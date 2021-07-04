package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.vo.BoxStandardSelectVo;
import com.ruoyi.project.storage.domain.vo.BoxStandardVo;

import java.util.List;
import java.util.Map;

/**
 * 箱子规格Mapper接口
 *
 * @author yanjiaqi
 * @date 2020.05.07
 */
public interface BoxStandardMapper {

    /**
     * 查询箱子规格列表
     *
     * @param boxStandard 箱子规格
     * @return 箱子规格集合
     */
    List<BoxStandardVo> selectBoxStandardList(BoxStandardVo boxStandard);

    /**
     * 新增箱子规格
     *
     * @param boxStandard 箱子规格
     * @return 修改条数
     */
    int insertBoxStandard(BoxStandardVo boxStandard);

    /**
     * 批量删除箱子规格
     *
     * @param map ids：需要删除的箱子规格id
     *            user：操作人
     *            time：操作时间
     * @return 修改条数
     */
    int deleteBoxStandardByIds(Map<String, Object> map);

    /**
     * 箱子规格下拉列表
     *
     * @return 箱子规格列表
     */
    List<BoxStandardSelectVo> selectBoxStandard();

    /**
     * 通过箱子规格查询箱子规格信息
     *
     * @param boxStandard 箱子规格
     * @return 箱子规格信息
     */
    BoxStandardSelectVo selectBoxStandardByStandard(BoxStandardVo boxStandard);

    /**
     * 通过箱子规格查询箱子每月积分单价
     *
     * @param boxStandard 箱子规格
     * @return 每月积分单价
     */
    Long selectBoxUnitPriceByStandard(String boxStandard);

    /**
     * 通过箱子id查询规格下有无箱子
     *
     * @param ids 需要删除的箱子规格id
     * @return 返回修改条数
     */
    int selectBoxInfoCountByIds(Long[] ids);

}
