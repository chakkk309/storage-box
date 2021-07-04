package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.vo.BoxInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 箱子信息Mapper接口
 *
 * @author yanjiaqi
 * @date 2020.05.08
 */
public interface BoxInfoMapper {

    /**
     * 查询箱子信息列表
     *
     * @param boxInfo 箱子信息
     * @return 箱子信息集合
     */
    List<BoxInfoVo> selectBoxInfoList(BoxInfoVo boxInfo);

    /**
     * 新增箱子信息
     *
     * @param boxInfo 箱子信息
     * @return 修改条数
     */
    int insertBoxInfo(BoxInfoVo boxInfo);

    /**
     * 批量删除箱子信息
     *
     * @param map ids：需要删除的箱子信息id
     *            user：操作人
     *            time：操作时间
     * @return 修改条数
     */
    int deleteBoxInfoByIds(Map<String, Object> map);

    /**
     * 批量新增箱子信息
     *
     * @param list 箱子信息列表
     * @return 修改条数
     */
    int batchInsertBoxInfo(List<BoxInfoVo> list);

    /**
     * 查询当前规格下的所有可用箱子列表
     *
     * @param boxStandard 箱子规格
     * @return 箱子信息列表
     */
    List<BoxInfoVo> selectUsedBoxInfo(String boxStandard);

    /**
     * 更新箱子信息
     *
     * @param boxInfoVo 箱子信息
     * @return 修改条数
     */
    int updateBoxInfo(BoxInfoVo boxInfoVo);

    /**
     * 通过箱子id查询有无已完成订单
     *
     * @param ids 需要删除的箱子id数组
     * @return 修改条数
     */
    int selectBoxOrderCountByIds(Long[] ids);

    /**
     * 通过箱子编号更新箱子信息（ 修改箱子信息表未使用、清空使用人）
     *
     * @param boxCode 箱子编号
     * @return 修改条数
     */
    int updateBoxInfoByBoxId(Long boxCode);

}
