package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.vo.OrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单Mapper接口
 *
 * @author yanjiaqi
 * @date 2020.05.12
 */
public interface OrderMapper {

    /**
     * 查询订单列表（手机端）
     *
     * @param orderVo 订单
     * @return 订单列表
     */
    List<OrderVo> selectAppOrderList(OrderVo orderVo);

    /**
     * 查询订单详情（手机端）
     *
     * @param id     订单id
     * @param userId 用户id
     * @return 订单
     */
    OrderVo selectAppOrderById(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 查询订单列表（后台端）
     *
     * @param orderVo 订单
     * @return 订单列表
     */
    List<OrderVo> selectBackendOrderList(OrderVo orderVo);

    /**
     * 查询订单详情（后台端）
     *
     * @param id 订单id
     * @return 订单
     */
    OrderVo selectBackendOrderById(Long id);

    /**
     * 空箱预约（新增订单)
     *
     * @param orderVo 订单列表
     * @return 修改条数
     */
    int insertOrder(OrderVo orderVo);

    /**
     * 订单操作
     *
     * @param map id：订单id
     *            operate：操作类型
     *            version：版本号
     *            userType：用户类型
     *            userId：当前订单用户id
     * @return 修改条数
     */
    int operateOrderByIds(Map<String, Object> map);

    /**
     * 手机端订单操作10
     *
     * @param map id：订单id
     *            operate：操作类型
     *            version：版本号
     *            userType：用户类型
     *            userId：当前订单用户id
     * @return 修改条数
     */
    int operateAppDelete(Map<String, Object> map);

    /**
     * 批量删除订单
     *
     * @param map ids：需要删除的订单id
     *            user：操作人
     *            time：操作时间
     * @return 修改条数
     */
    int deleteOrderByIds(Map<String, Object> map);

    /**
     * 批量插入订单历史表
     *
     * @param map ids：需要插入订单历史表的订单id
     *            user：操作人
     *            time：操作时间
     * @return 修改条数
     */
    int insertOrderHistoryByIds(Map<String, Object> map);

    /**
     * 单条插入订单历史表
     *
     * @param id 订单id
     * @return 修改条数
     */
    int insertOrderHistoryById(Long id);

    /**
     * 通过订单id找到对应的箱子编号
     *
     * @param id 订单id
     * @return 箱子编号
     */
    Long findBoxCode(Long id);

    /**
     * 通过订单id设置手机端删除标记
     *
     * @param id 订单id
     * @return 修改条数
     */
    int setAppDelete(Long id);

    /**
     * 更新重箱相关字段
     *
     * @param orderVo 订单列表
     * @return 修改条数
     */
    int updateHeavyOrder(OrderVo orderVo);

    /**
     * 通过订单id获取当前订单的用户id
     *
     * @param id 用户id
     * @return 当前订单的用户id
     */
    Long findUserIdByOrderId(Long id);

}
