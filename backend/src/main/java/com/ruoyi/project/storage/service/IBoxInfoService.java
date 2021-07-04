package com.ruoyi.project.storage.service;

import com.ruoyi.project.storage.domain.vo.BoxInfoVo;

import java.util.List;

/**
 * 箱子信息Service接口
 *
 * @author yanjiaqi
 * @date 2020.05.09
 */
public interface IBoxInfoService {

    /**
     * 查询箱子信息列表
     *
     * @param boxInfo 箱子信息
     * @return 箱子信息集合
     */
    List<BoxInfoVo> selectBoxInfoVoList(BoxInfoVo boxInfo);

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
     * @param ids 需要删除的箱子信息id数组
     * @return 修改条数
     */
    int deleteBoxInfoByIds(Long[] ids);

    /**
     * 查询具体规格下箱子信息
     *
     * @param boxStandard 箱子规格
     * @return 修改条数
     */
    List<BoxInfoVo> selectUsedBoxInfo(String boxStandard);

    /**
     * 更新箱子信息
     *
     * @param boxInfoVo 箱子信息
     * @return 修改条数
     */
    int updateBoxInfo(BoxInfoVo boxInfoVo);

}
