package com.ruoyi.project.storage.service;

import com.ruoyi.project.storage.domain.vo.BoxInfoVo;
import com.ruoyi.project.storage.domain.vo.BoxStandardSelectVo;
import com.ruoyi.project.storage.domain.vo.BoxStandardVo;

import java.util.List;

/**
 * 箱子规格Service接口
 *
 * @author yanjiaqi
 * @date 2020.04.28
 */
public interface IBoxStandardService {

    /**
     * 查询箱子规格列表
     *
     * @param boxStandard 箱子规格
     * @return 箱子规格集合
     */
    List<BoxStandardVo> selectBoxStandardVoList(BoxStandardVo boxStandard);

    /**
     * 新增箱子规格
     *
     * @param boxStandard 箱子规格
     * @return 修改条数
     */
    int insertBoxStandard(BoxStandardVo boxStandard);

    /**
     * 初始化箱子信息
     *
     * @param boxStandardVo 箱子规格
     * @return 修改条数集合
     */
    List<BoxInfoVo> initBoxInfoList(BoxStandardVo boxStandardVo);

    /**
     * 批量删除箱子规格
     *
     * @param ids 需要删除的箱子规格ID数组
     * @return 修改条数
     */
    int deleteBoxStandardByIds(Long[] ids);

    /**
     * 箱子规格下拉列表
     *
     * @return 箱子规格列表
     */
    List<BoxStandardSelectVo> selectBoxStandard();

}
