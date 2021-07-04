package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.common.enums.RecordPointWay;
import com.ruoyi.project.common.util.ParameterUtil;
import com.ruoyi.project.storage.domain.Advertisement;
import com.ruoyi.project.storage.domain.User;
import com.ruoyi.project.storage.mapper.AdvertisementMapper;
import com.ruoyi.project.storage.mapper.UserMapper;
import com.ruoyi.project.storage.service.IAdvertisementService;
import com.ruoyi.project.storage.service.IPointRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 广告Service业务层处理
 *
 * @author yanjiaqi
 * @date 2020.04.28
 */
@Service
@Slf4j
public class AdvertisementServiceImpl implements IAdvertisementService {

    /**
     * 广告标题最大长度20
     */
    final String TITLE_MAX = "20";

    /**
     * 广告内容最大长度1000
     */
    final String CONTENT_Max = "1000";

    /**
     * 广告积分限制为正整数
     */
    final Long POINT_Min = 0L;

    /**
     * 序号限制为正整数
     */
    final Long SORT_NO_Min = 0L;

    /**
     * 积分记录Mapper
     */
    private final IPointRecordService pointRecordService;

    /**
     * 用户Mapper
     */
    private final UserMapper userMapper;

    /**
     * 广告Mapper
     */
    private final AdvertisementMapper advertisementMapper;

    /**
     * 构造方法注入
     *
     * @param pointRecordService  积分记录Service
     * @param userMapper          用户信息Mapper
     * @param advertisementMapper 广告Mapper
     */
    @Autowired
    public AdvertisementServiceImpl(IPointRecordService pointRecordService, UserMapper userMapper,
                                    AdvertisementMapper advertisementMapper) {
        // 赋值
        this.pointRecordService = pointRecordService;
        // 赋值
        this.userMapper = userMapper;
        // 赋值
        this.advertisementMapper = advertisementMapper;
    }

    /**
     * 查询广告列表
     *
     * @param advertisement 广告
     * @return 广告集合
     */
    @Override
    public List<Advertisement> selectAdvertisementList(Advertisement advertisement) {
        // 返回广告列表数据
        return advertisementMapper.selectAdvertisementList(advertisement);
    }

    /**
     * 新增广告
     *
     * @param advertisement 广告
     * @return 结果
     */
    @Override
    public int insertAdvertisement(Advertisement advertisement) {
        // 广告字段校验
        judgeAdvertisement(advertisement);
        // 设置创建基础字段
        ParameterUtil.setCreateEntity(advertisement);
        // 设置启用
        advertisement.setIsEnable(0);
        // 设置版本号
        advertisement.setVersion(0L);
        // 设置插入条数
        return advertisementMapper.insertAdvertisement(advertisement);
    }

    /**
     * 修改广告
     *
     * @param advertisement 广告
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAdvertisement(Advertisement advertisement) {
        // 广告字段校验
        judgeAdvertisement(advertisement);
        // 设置更新时间
        advertisement.setUpdateTime(DateUtils.getNowDate());
        // 设置更新值
        advertisement.setUpdateBy(SecurityUtils.getUsername());
        // 修改条数
        int count = advertisementMapper.updateAdvertisement(advertisement);
        // 乐观锁判断
        if (count == 0) {
            // 打log
            log.error("AdvertisementServiceImpl.updateAdvertisement failed：乐观锁");
            // 抛出异常标记乐观锁
            throw new CustomException("广告【" + advertisement.getTitle() + "】已被他人率先修改，请刷新后重试", HttpStatus.CONFLICT);
        }
        // 返回修改数据
        return count;
    }

    /**
     * 批量删除广告
     *
     * @param ids 需要删除的广告id数组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAdvertisementByIds(Long[] ids) {
        // 判断删除条数
        int count = advertisementMapper.deleteAdvertisementByIds(ParameterUtil.getBatchUpdateMapByIds(ids));
        // 如果删除条数不等于需要操作的条数
        if (count != ids.length) {
            // 抛出异常
            throw new CustomException("请勿删除还未停用的广告", HttpStatus.ERROR);
        }
        // 返回修改条数
        return count;
    }

    /**
     * 批量启用/停用广告
     *
     * @param operate 操作类型（“enable”：启用； “disable”：停用）
     * @param ids     需要启用/停用的广告id数组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int operateAdvertisementByIds(String operate, Long[] ids) {
        // 判断启用/停用条数
        int count = advertisementMapper.operateAdvertisementByIds(ParameterUtil.getBatchUpdateMapByOperateIds(operate, ids));
        // 如果启用/停用条数不等于需要操作的条数
        if (count != ids.length) {
            // 抛出异常
            throw new CustomException("请刷新后重试", HttpStatus.CONFLICT);
        }
        // 返回修改条数
        return count;
    }

    /**
     * 广告积分获取
     *
     * @param advertisement 广告对象
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int getPointForUser(Advertisement advertisement) {
        // 如果传入广告积分与表中查询到的积分不同
        if (!advertisement.getPoints().equals(advertisementMapper.selectAdvertisementById(advertisement.getId()))) {
            // 手动抛异常
            throw new CustomException("请检查当前广告积分是否正确", HttpStatus.ERROR);
        }
        // 实例化用户
        User user = new User();
        // 设置更新基础字段
        ParameterUtil.setUpdateEntity(user);
        // 设置积分（追加积分）
        user.setCurrentPoints(advertisement.getPoints());
        // 设置用户id
        user.setUserId(SecurityUtils.getUserId());
        // 操作用户积分并返回操作条数
        int userOperationCount = userMapper.operateUserPoints(user);
        // 判断更新条数
        if (userOperationCount > 0) {
            // 返回插入积分记录表结果
            return pointRecordService.insertPointRecord(RecordPointWay.ADVERTISEMENT, user.getUserId(),
                    advertisement.getPoints(), advertisement.getId(), null);
        } else {
            // 手动抛出异常
            throw new CustomException("广告积分获取失败，请联系管理员", HttpStatus.ERROR);
        }
    }

    /**
     * 广告字段限制
     *
     * @param advertisement 广告信息
     */
    public void judgeAdvertisement(Advertisement advertisement) {
        // 判断广告标题是否为空
        if (advertisement.getTitle() != null) {
            // 如果标题长度大于20
            if (TITLE_MAX.equals(advertisement.getTitle())) {
                // 手动抛异常
                throw new CustomException("标题长度不能超过20");
            }
        } else {
            // 手动抛异常
            throw new CustomException("广告标题不可为空");
        }
        // 判断广告内容是否为空
        if (advertisement.getContent() != null) {
            // 如果广告内容大于1000
            if (CONTENT_Max.equals(advertisement.getContent())) {
                // 手动抛异常
                throw new CustomException("广告内容不能超过1000");
            }
        } else {
            // 手动抛异常
            throw new CustomException("广告内容不可为空");
        }
        // 判断奖励积分是否为空
        if (advertisement.getPoints() != null) {
            // 如果奖励积分小于等于0
            if (POINT_Min >= advertisement.getPoints()) {
                // 手动抛异常
                throw new CustomException("奖励积分必须为正");
            }
        } else {
            // 手动抛异常
            throw new CustomException("奖励积分不可为空");
        }
        // 判断序号是否为空
        if (advertisement.getSortNo() != null) {
            // 如果序号小于等于0
            if (SORT_NO_Min >= advertisement.getSortNo()) {
                // 手动抛异常
                throw new CustomException("序号需要大于等于0");
            }
        } else {
            // 手动抛异常
            throw new CustomException("序号不可为空");
        }
    }
}
