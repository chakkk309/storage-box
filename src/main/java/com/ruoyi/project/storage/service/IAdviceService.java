package com.ruoyi.project.storage.service;

import com.ruoyi.project.storage.domain.vo.AdviceVo;

import java.util.List;

/**
 * 意见建议Service接口
 *
 * @author yanjiaqi
 * @date 2020.04.29
 */
public interface IAdviceService {

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
