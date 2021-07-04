package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.vo.AdviceVo;

import java.util.List;

/**
 * 意见建议接口Mapper
 *
 * @author yanjiaqi
 * @date 2020.05.09
 */
public interface AdviceMapper {

    /**
     * 查询意见建议列表
     *
     * @param advice 意见建议
     * @return 意见建议集合
     */
    List<AdviceVo> selectAdviceList(AdviceVo advice);

    /**
     * 新增意见建议
     *
     * @param advice 意见建议
     * @return 修改条数
     */
    int insertAdvice(AdviceVo advice);

}
