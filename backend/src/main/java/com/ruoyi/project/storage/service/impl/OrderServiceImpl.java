package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.common.enums.OrderEnum;
import com.ruoyi.project.common.enums.RecordPointWay;
import com.ruoyi.project.common.util.ParameterUtil;
import com.ruoyi.project.common.util.SeqGeneratorUtil;
import com.ruoyi.project.storage.domain.User;
import com.ruoyi.project.storage.domain.vo.BoxInfoVo;
import com.ruoyi.project.storage.domain.vo.OrderVo;
import com.ruoyi.project.storage.mapper.BoxInfoMapper;
import com.ruoyi.project.storage.mapper.BoxStandardMapper;
import com.ruoyi.project.storage.mapper.OrderMapper;
import com.ruoyi.project.storage.mapper.UserMapper;
import com.ruoyi.project.storage.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 订单Service操作业务层处理
 *
 * @author yanjiaqi
 * @date 2020.05.12
 */
@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    /**
     * 箱子信息Mapper
     */
    private final BoxInfoMapper boxInfoMapper;

    /**
     * 订单列表Mapper
     */
    private final OrderMapper orderMapper;

    /**
     * 箱子规格Mapper
     */
    private final BoxStandardMapper boxStandardMapper;

    /**
     * 箱子信息Service
     */
    private final BoxInfoServiceImpl boxInfoService;

    /**
     * 积分记录Service
     */
    private final PointRecordServiceImpl pointRecordService;

    /**
     * 用户Service
     */
    private final UserMapper userMapper;

    /**
     * 构造方法注入
     *
     * @param boxInfoMapper      箱子信息Mapper
     * @param orderMapper        订单Mapper
     * @param boxStandardMapper  箱子规格Mapper
     * @param boxInfoService     箱子信息Service
     * @param pointRecordService 积分记录Service
     * @param userMapper         用户Mapper
     */
    public OrderServiceImpl(BoxInfoMapper boxInfoMapper, OrderMapper orderMapper, BoxStandardMapper boxStandardMapper,
                            BoxInfoServiceImpl boxInfoService, PointRecordServiceImpl pointRecordService, UserMapper userMapper) {
        // 赋值
        this.boxInfoMapper = boxInfoMapper;
        // 赋值
        this.orderMapper = orderMapper;
        // 赋值
        this.boxStandardMapper = boxStandardMapper;
        // 赋值
        this.boxInfoService = boxInfoService;
        // 赋值
        this.pointRecordService = pointRecordService;
        // 赋值
        this.userMapper = userMapper;
    }

    /**
     * 查询订单列表（手机端）
     *
     * @param orderVo 订单
     * @return 结果
     */
    @Override
    public List<OrderVo> selectAppOrderList(OrderVo orderVo) {
        // 设置用户id字段
        orderVo.setUserId(SecurityUtils.getUserId());
        // 返回用户列表数据
        return orderMapper.selectAppOrderList(orderVo);
    }

    /**
     * 查询订单详情（手机端）
     *
     * @param id 订单id
     * @return 结果
     */
    @Override
    public OrderVo selectAppOrder(Long id) {
        // 返回修改条数
        return orderMapper.selectAppOrderById(id, SecurityUtils.getUserId());
    }

    /**
     * 查询订单列表（后台端）
     *
     * @param orderVo 订单
     * @return 结果
     */
    @Override
    public List<OrderVo> selectBackendOrderList(OrderVo orderVo) {
        // 设置用户id字段
        orderVo.setUserId(SecurityUtils.getUserId());
        // 返回用户列表数据
        return orderMapper.selectBackendOrderList(orderVo);
    }

    /**
     * 查询订单详情（后台端）
     *
     * @param id 订单id
     * @return 结果
     */
    @Override
    public OrderVo selectBackendOrder(Long id) {
        // 返回修改条数
        return orderMapper.selectBackendOrderById(id);
    }

    /**
     * 新增订单且更新当前客户积分
     *
     * @param orderVo   订单信息
     * @param boxInfoVo 箱子信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertOrder(OrderVo orderVo, BoxInfoVo boxInfoVo) {
        // 判断每月积分单价是否正确
        if (!orderVo.getBoxUnitPrice().equals(boxStandardMapper.selectBoxUnitPriceByStandard(orderVo.getBoxStandard()))) {
            // 手动抛异常
            throw new CustomException("当前箱子规格与其单价不符", HttpStatus.ERROR);
        }
        // 判断租赁时长是否大于1个月
        if (orderVo.getLeaseDuration() >= 1) {
            // 计算当前操作所需积分
            long point = orderVo.getBoxUnitPrice() * orderVo.getLeaseDuration();
            // 判断传入当前操作积分是否正确
            if (!orderVo.getTotalPrice().equals(point)) {
                // 手动抛异常
                throw new CustomException("当前箱子所需总积分与后台数据不符，请检查后输入", HttpStatus.ERROR);
            }
            // 判断当前用户积分是否大于订单操作所需积分
            if (point < userMapper.getCurrentPoints(SecurityUtils.getUserId())) {
                // 设置空箱预约相关字段
                setAppEmptyCallOrder(orderVo, point, boxInfoVo);
            } else {
                // 积分不够，手动抛异常
                throw new CustomException("当前积分不足，请重新选择");
            }
        } else {
            // 租赁时长不够，手动抛异常
            throw new CustomException("请重新选择租赁时长");
        }
        // 新增订单，返回修改条数
        int count = orderMapper.insertOrder(orderVo);
        // 如果修改条数大于0，更新当前客户积分（积分记录表）
        if (count > 0) {
            // 创建一个user对象
            User user = new User();
            // 设置用户id
            user.setUserId(SecurityUtils.getUserId());
            // 设置更新时间
            user.setUpdateTime(DateUtils.getNowDate());
            // 设置更新人
            user.setUpdateBy(SecurityUtils.getUsername());
            // 当前客户积分
            Long points = userMapper.selectUserPointByUserId(SecurityUtils.getUserId());
            // 打log
            log.info("当前客户积分为" + points + ",当前操作使用积分为" + orderVo.getBoxUnitPrice() * orderVo.getLeaseDuration());
            // 设置当前客户积分变更
            user.setCurrentPoints(points + orderVo.getBoxUnitPrice() * orderVo.getLeaseDuration() * (-1));
            // 更新当前客户积分
            userMapper.updateUserPoints(user);
        }
        // 如果修改条数为0
        if (count == 0) {
            // 手动抛异常
            throw new CustomException("提交订单失败，请联系管理员");
        }
    }

    /**
     * 自动分配箱子
     *
     * @param orderVo 箱子信息
     * @return 修改条数
     */
    @Transactional(rollbackFor = Exception.class)
    public int autoAssign(OrderVo orderVo) {
        // 获取选中的箱子规格下可用的箱子列表
        List<BoxInfoVo> list = boxInfoService.selectUsedBoxInfo(orderVo.getBoxStandard());
        // 判定list是否为空
        if (list != null && !list.isEmpty()) {
            // 如果不为空，获得一个箱子
            BoxInfoVo boxInfoVo = list.get(0);
            // 设置箱子已被使用
            boxInfoVo.setIsUsed(1);
            // 设置箱子使用人
            boxInfoVo.setUsedBy(SecurityUtils.getUserId());
            // 设置更新基础字段
            ParameterUtil.setUpdateEntity(boxInfoVo);
            // 更新箱子信息信息表
            int count = boxInfoService.updateBoxInfo(boxInfoVo);
            // 判定箱子更新结果（1：拿到箱子）
            if (count == 1) {
                // 插入订单表
                ((IOrderService) AopContext.currentProxy()).insertOrder(orderVo, boxInfoVo);
            }
            // 如果为0，进入补偿机制
            if (count == 0) {
                // 补偿机制
                this.autoAssign(orderVo);
            }
        } else {
            // 当前规格下无箱子，返回提示信息
            throw new CustomException("箱子库存不足，请重新选择", HttpStatus.ERROR);
        }
        // 新增积分使用记录
        return pointRecordService.insertPointRecord(RecordPointWay.ORDER, SecurityUtils.getUserId(),
                orderVo.getBoxUnitPrice() * orderVo.getLeaseDuration() * (-1), null, orderVo.getId());
    }

    /**
     * 订单操作
     *
     * @param id      需要操作的订单id
     * @param operate 操作类型
     * @param version 版本号
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int operateOrderByIds(Long id, Integer operate, Long version, OrderVo orderVo) {
        // 判断操作类型是否为：3（App端收取空箱）、4（手机端发送重箱）、8（手机端收取重箱）、9（手机端发送空箱）、2（后台端发送空箱）、5（后台端收取重箱）、7（后台端发送重箱）之一
        if (OrderEnum.APP_GET_EMPTY_BOX.equals(judge(operate))
                || OrderEnum.APP_SEND_HEAVY_BOX.equals(judge(operate))
                || OrderEnum.APP_CALL_HEAVY_BOX.equals(judge(operate))
                || OrderEnum.APP_SEND_EMPTY_BOX.equals(judge(operate))
                || OrderEnum.BACKEND_SEND_EMPTY_BOX.equals(judge(operate))
                || OrderEnum.BACKEND_CALL_HEAVY_BOX.equals(judge(operate))
                || OrderEnum.BACKEND_SEND_HEAVY_BOX.equals(judge(operate))) {
            // 插入订单历史表
            orderMapper.insertOrderHistoryById(id);
        }
        // 如果操作类型为6，即手机端预约提取
        if (OrderEnum.APP_ORDER_CALL.equals(judge(operate))) {
            // 校验预约提取字段
            checkAppHeavyBoxCall(orderVo);
            // 插入历史表
            orderMapper.insertOrderHistoryById(id);
            // 设置预约提取相关字段
            OrderVo appHeavyBoxCall = setAppHeavyBoxCallOrder(orderVo, id);
            // 更新重箱相关字段
            orderMapper.updateHeavyOrder(appHeavyBoxCall);
        }
        // 如果操作类型为10且用户类型是backend（01），即后台端收取空箱
        if (OrderEnum.BACKEND_CALL_EMPTY_BOX.equals(judge(operate))) {
            // 插入订单历史表
            orderMapper.insertOrderHistoryById(id);
            // 修改箱子信息表（设置未使用、清空使用人）
            boxInfoMapper.updateBoxInfoByBoxId(orderMapper.findBoxCode(id));
        }
        // 如果操作类型为10且用户类型是APP（02），即手机端删除订单
        if (OrderEnum.APP_DELETE_ORDER.equals(judge(operate))) {
            // 插入订单历史表
            orderMapper.insertOrderHistoryById(id);
        }
        // 订单更新条数
        int count = updateOrder(id, operate, version, judge(operate));
        // 乐观锁判断
        if (count == 0) {
            // 打log，乐观锁
            log.error("OrderServiceImpl.operateOrder failed:乐观锁");
            // 抛出异常标记乐观锁
            throw new CustomException("已有他人优先操作，请刷新后重试", HttpStatus.CONFLICT);
        }
        // 返回修改条数
        return count;
    }

    /**
     * 校验预约提取字段
     *
     * @param orderVo 预约提取相关字段（重箱）
     */
    public void checkAppHeavyBoxCall(OrderVo orderVo) {
        // 如果预约提取姓名为空
        if (orderVo.getHeavyBoxCallName() == null) {
            // 手动抛异常
            throw new CustomException("重箱提取姓名不可为空");
        }
        // 如果预约提取电话为空
        if (orderVo.getHeavyBoxCallPhone() == null) {
            // 手动抛异常
            throw new CustomException("重箱提取电话不可为空");
        }
        // 如果重箱提取地址为空
        if (orderVo.getHeavyBoxCallAddress() == null) {
            // 手动抛异常
            throw new CustomException("重箱提取地址不可为空");
        }
        // 如果预约提取时间为空
        if (orderVo.getHeavyBoxCallTime() == null) {
            // 手动抛异常
            throw new CustomException("重箱提取时间不可为空");
        }
        // 如果预约提取时段不为空
        if (orderVo.getHeavyBoxCallInterval() != null) {
            // 打log
            log.info("预约提取时段为" + orderVo.getHeavyBoxCallInterval());
            // 分割开始时段和结束时段
            String[] args = orderVo.getHeavyBoxCallInterval().split("-");
            // 开始时段分割小时
            String[] startHour = args[0].split(":");
            // 结束时段分割小时
            String[] endHour = args[1].split(":");
            // 定义时段分割标准
            int cutCount = 4;
            // 打log
            log.info("开始时段长度为" + startHour.length);
            // 打log
            log.info("结束时段长度为" + endHour.length);
            // 如果分割符的数量与预期不同
            if ((startHour.length + endHour.length) != cutCount) {
                // 手动抛异常
                throw new CustomException("请填写合理的时间");
            }
            // 如果开始时间大于结束时间
            if (Integer.parseInt(startHour[0]) > Integer.parseInt(endHour[0])) {
                // 手动抛异常
                throw new CustomException("请填写合理的时间");
            }
            // 当开始与结束否为同一小时时
            if (Integer.parseInt(startHour[0]) == Integer.parseInt(endHour[0])) {
                // 如果开始大于结束时间
                if (Integer.parseInt(startHour[1]) > Integer.parseInt(endHour[1])) {
                    // 手动抛异常
                    throw new CustomException("请填写合理的时间");
                }
            }
        }
    }

    /**
     * 设置空箱预约相关字段
     *
     * @param orderVo   订单信息
     * @param point     积分
     * @param boxInfoVo 箱子信息
     */
    public void setAppEmptyCallOrder(OrderVo orderVo, Long point, BoxInfoVo boxInfoVo) {
        // 设置基础创建字段
        ParameterUtil.setCreateEntity(orderVo);
        // 设置用户id
        orderVo.setUserId(SecurityUtils.getUserId());
        // 设置空箱上门下单时间
        orderVo.setEmptyBoxOrderTime(DateUtils.getNowDate());
        // 设置订单编号（年月日+6位序列）
        orderVo.setOrderCode(Long.valueOf(SeqGeneratorUtil.seqGenerator(DateUtils.getNowDateStr(), 6)));
        // 设置租赁积分 （当前时间+租赁月份）
        orderVo.setTotalPrice(point);
        // 设置订单状态为已下上门单
        orderVo.setStatus(1);
        // 设置箱子id
        orderVo.setBoxId(boxInfoVo.getId());
        // 设置箱子code
        orderVo.setBoxCode(boxInfoVo.getBoxCode());
        // 设置订单手机端删除标记
        orderVo.setAppDelFlag(0);
        // 设置订单后台端删除标记
        orderVo.setBackendDelFlag(0);
    }

    /**
     * 设置重箱提取相关字段
     *
     * @param orderVo 订单类型
     * @param id      需要操作的订单id
     * @return 订单
     */
    public OrderVo setAppHeavyBoxCallOrder(OrderVo orderVo, Long id) {
        // 创建订单对象
        OrderVo order = new OrderVo();
        // 写入订单id
        order.setId(id);
        // 写入重箱提取下单时间
        order.setHeavyBoxOrderTime(DateUtils.getNowDate());
        // 写入重箱提取下单时间
        order.setHeavyBoxCallName(orderVo.getHeavyBoxCallName());
        // 写入重箱提取姓名
        order.setHeavyBoxCallPhone(orderVo.getHeavyBoxCallPhone());
        // 写入重箱提取地址
        order.setHeavyBoxCallAddress(orderVo.getHeavyBoxCallAddress());
        // 写入重箱提取时间（年月日）
        order.setHeavyBoxCallTime(orderVo.getHeavyBoxCallTime());
        // 写入重箱提取时段
        order.setHeavyBoxCallInterval(orderVo.getHeavyBoxCallInterval());
        // 返回订单
        return order;
    }

    /**
     * 更新订单操作
     *
     * @param id      订单id
     * @param operate 操作类型
     * @param version 版本号
     */
    @Transactional(rollbackFor = Exception.class)
    int updateOrder(Long id, Integer operate, Long version, OrderEnum orderEnum) {
        // 设置用户类型（01：后台端；02：手机端）
        String userType = userMapper.selectUserTypeByUserId(SecurityUtils.getUserId());
        // 设置用户id
        Long userId = orderMapper.findUserIdByOrderId(id);
        // 定义订单更新条数
        int count;
        // 如果该操作为手机端删除订单（10）
        if (orderEnum.equals(OrderEnum.APP_DELETE_ORDER)) {
            // 更新订单状态（手机端删除订单10)
            count = orderMapper.operateAppDelete(ParameterUtil.getBatchUpdateMap(id, operate, version, userType, userId));
        } else {
            // 更新订单状态（除手机端删除订单10的之外）
            count = orderMapper.operateOrderByIds(ParameterUtil.getBatchUpdateMap(id, operate, version, userType, userId));
        }
        return count;
    }

    /**
     * 判断具体操作类型
     *
     * @param operate 操作类型
     * @return 相应枚举结果
     */
    public OrderEnum judge(Integer operate) {
        // 获取用户类型（01：后台端，02：手机端）
        String userType = userMapper.selectUserTypeByUserId(SecurityUtils.getUserId());
        // 如果操作类型为2用户类型是backend（01），即后台端发送空箱
        if (operate.equals(OrderEnum.BACKEND_SEND_EMPTY_BOX.getOperate()) &&
                (userType).equals(OrderEnum.BACKEND_SEND_EMPTY_BOX.getTerminal())) {
            // 返回相应状态
            return OrderEnum.BACKEND_SEND_EMPTY_BOX;
        }
        // 如果操作类型为3用户类型是app（02），即手机端收取空箱
        if (operate.equals(OrderEnum.APP_GET_EMPTY_BOX.getOperate()) &&
                (userType).equals(OrderEnum.APP_GET_EMPTY_BOX.getTerminal())) {
            // 返回相应状态
            return OrderEnum.APP_GET_EMPTY_BOX;
        }
        // 如果操作类型为4且用户类型app（02），即手机端发送重箱
        if (operate.equals(OrderEnum.APP_SEND_HEAVY_BOX.getOperate()) &&
                (userType).equals(OrderEnum.APP_SEND_HEAVY_BOX.getTerminal())) {
            // 返回相应状态
            return OrderEnum.APP_SEND_HEAVY_BOX;
        }
        // 如果操作类型为5用户类型是backend（01），即后台端收取重箱
        if (operate.equals(OrderEnum.BACKEND_CALL_HEAVY_BOX.getOperate()) &&
                (userType).equals(OrderEnum.BACKEND_CALL_HEAVY_BOX.getTerminal())) {
            // 返回相应状态
            return OrderEnum.BACKEND_CALL_HEAVY_BOX;
        }
        // 如果操作类型为6用户类型是app（02），即手机端预约提取
        if (operate.equals(OrderEnum.APP_ORDER_CALL.getOperate()) &&
                (userType).equals(OrderEnum.APP_ORDER_CALL.getTerminal())) {
            // 返回相应状态
            return OrderEnum.APP_ORDER_CALL;
        }
        // 如果操作类型为7且用户类型是backend（01），即后台端发送重箱
        if (operate.equals(OrderEnum.BACKEND_SEND_HEAVY_BOX.getOperate()) &&
                (userType).equals(OrderEnum.BACKEND_SEND_HEAVY_BOX.getTerminal())) {
            // 返回相应状态
            return OrderEnum.BACKEND_SEND_HEAVY_BOX;
        }
        // 如果操作类型为8且用户类型是app（02），即手机端收取重箱
        if (operate.equals(OrderEnum.APP_CALL_HEAVY_BOX.getOperate()) &&
                (userType).equals(OrderEnum.APP_CALL_HEAVY_BOX.getTerminal())) {
            // 返回相应状态
            return OrderEnum.APP_CALL_HEAVY_BOX;
        }
        // 如果操作类型为9且用户类型是app（02），即手机端发送空箱
        if (operate.equals(OrderEnum.APP_SEND_EMPTY_BOX.getOperate()) &&
                (userType).equals(OrderEnum.APP_SEND_EMPTY_BOX.getTerminal())) {
            // 返回相应状态
            return OrderEnum.APP_SEND_EMPTY_BOX;
        }
        // 如果操作类型为10且用户类型是backend（01），即后台端收取重箱
        if (operate.equals(OrderEnum.BACKEND_CALL_EMPTY_BOX.getOperate()) &&
                (userType).equals(OrderEnum.BACKEND_CALL_EMPTY_BOX.getTerminal())) {
            // 返回相应状态
            return OrderEnum.BACKEND_CALL_EMPTY_BOX;
        }
        // 如果操作类型为10且用户类型是APP（02），即手机端删除订单
        if (operate.equals(OrderEnum.APP_DELETE_ORDER.getOperate()) &&
                (userType).equals(OrderEnum.APP_DELETE_ORDER.getTerminal())) {
            // 返回相应状态
            return OrderEnum.APP_DELETE_ORDER;
        } else {
            // 手动抛异常
            throw new CustomException("操作异常，请填写正确的订单操作");
        }
    }

    /**
     * 删除订单
     *
     * @param ids 需要删除的订单id数组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteOrderByIds(Long[] ids) {
        // 判断删除订单的数组是否为空
        if (ids == null || ids.length == 0) {
            // 如果为空，手动抛异常
            throw new CustomException("请填写要删除的订单id");
        }
        // 批量插入历史表
        int insertCount = orderMapper.insertOrderHistoryByIds(ParameterUtil.getBatchUpdateMapByIds(ids));
        // 插入历史表的数量不等于订单数组的长度
        if (insertCount != ids.length) {
            // 手动抛异常
            throw new CustomException("插入失败，请重试");
        }
        // 删除的订单
        int deleteCount = orderMapper.deleteOrderByIds(ParameterUtil.getBatchUpdateMapByIds(ids));
        // 已删除的条数不等于数组的长度
        if (deleteCount != ids.length) {
            // 手动抛异常
            throw new CustomException("请勿删除未完成的订单");
        }
        // 返回修改结果
        return deleteCount;
    }

}



