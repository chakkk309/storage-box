package com.ruoyi.project.storage.service;

import com.ruoyi.project.common.enums.RecordPointWay;
import com.ruoyi.project.storage.domain.PointRecord;

import java.util.List;

/**
 * 积分记录Service
 *
 * @author yanjiaqi
 * @date 2020.05.02
 */
public interface IPointRecordService {

    /**
     * 查询积分记录列表
     *
     * @param userId 用户id
     * @return 积分记录
     */
    List<PointRecord> selectRecordList(Long userId);

    /**
     * 新增积分记录
     *
     * @param recordPointWay  方式（1：注册赠送、2：活动获得、3：积分使用）
     * @param userId          用户id
     * @param points          积分（积分获得为正数，积分使用为负数）
     * @param advertisementId 广告id
     * @param orderId         订单id
     * @return 修改条数
     */
    int insertPointRecord(RecordPointWay recordPointWay, Long userId, Long points, Long advertisementId, Long orderId);

}
