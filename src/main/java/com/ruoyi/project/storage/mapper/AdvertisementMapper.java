package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.Advertisement;

import java.util.List;
import java.util.Map;

/**
 * 广告Mapper接口
 *
 * @author yanjiaqi
 * @date 2020.04.28
 */
public interface AdvertisementMapper {

    /**
     * 查询广告列表
     *
     * @param advertisement 广告
     * @return 广告集合
     */
    List<Advertisement> selectAdvertisementList(Advertisement advertisement);

    /**
     * 新增广告
     *
     * @param advertisement 广告
     * @return 修改条数
     */
    int insertAdvertisement(Advertisement advertisement);

    /**
     * 修改广告
     *
     * @param advertisement 广告
     * @return 修改条数
     */
    int updateAdvertisement(Advertisement advertisement);

    /**
     * 批量删除广告
     *
     * @param map ids：需要删除的广告id数组
     *            user：操作人
     *            time：操作时间
     * @return 修改条数
     */
    int deleteAdvertisementByIds(Map<String, Object> map);

    /**
     * 批量启用/停用广告
     *
     * @param map ids：需要启用/停用的广告id数组
     *            operate：操作类型（“enable”：启用；“disable”：停用）
     *            user：操作人
     *            time：操作时间
     * @return 修改条数
     */
    int operateAdvertisementByIds(Map<String, Object> map);

    /**
     * 通过广告id获取广告积分
     *
     * @param id 广告id
     * @return 广告积分
     */
    Long selectAdvertisementById(Long id);

}
