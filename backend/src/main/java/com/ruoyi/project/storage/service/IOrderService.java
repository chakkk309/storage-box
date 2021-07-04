package com.ruoyi.project.storage.service;

import com.ruoyi.project.storage.domain.vo.BoxInfoVo;
import com.ruoyi.project.storage.domain.vo.OrderVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单Service
 *
 * @author yanjiaqi
 * @date 2020.05.12
 */
public interface IOrderService {

    /**
     * 查询订单列表（后台端）
     *
     * @param order 订单
     * @return 修改条数
     */
    List<OrderVo> selectBackendOrderList(OrderVo order);

    /**
     * 查询订单列表（手机端）
     *
     * @param orderVo 订单
     * @return 修改条数
     */
    List<OrderVo> selectAppOrderList(OrderVo orderVo);

    /**
     * 查询订单详情（手机端）
     *
     * @param id 订单id
     * @return 修改条数
     */
    OrderVo selectAppOrder(Long id);

    /**
     * 查询订单详情（后台端）
     *
     * @param id 订单id
     * @return 修改条数
     */
    OrderVo selectBackendOrder(Long id);

    /**
     * 新增订单
     *
     * @param orderVo   订单信息
     * @param boxInfoVo 箱子信息
     */
    void insertOrder(OrderVo orderVo, BoxInfoVo boxInfoVo);

    /**
     * 订单操作
     *
     * @param id      需要操作的订单id
     * @param operate 操作类型
     * @param version 版本哈
     * @param orderVo 订单信息（重箱提取相关字段）
     * @return 修改条数
     */
    @Transactional(rollbackFor = Exception.class)
    int operateOrderByIds(Long id, Integer operate, Long version, OrderVo orderVo);

    /**
     * 删除订单
     *
     * @param orderIds 需要删除的订单id
     * @return 修改条数
     */
    @Transactional(rollbackFor = Exception.class)
    int deleteOrderByIds(Long[] orderIds);

}
