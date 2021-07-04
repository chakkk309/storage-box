package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.common.util.ParameterUtil;
import com.ruoyi.project.storage.domain.vo.AdviceVo;
import com.ruoyi.project.storage.mapper.AdviceMapper;
import com.ruoyi.project.storage.service.IAdviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 意见建议Service业务层处理
 *
 * @author yanjiaqi
 * @date 2020.04.29
 */
@Service
@Slf4j
public class AdviceServiceImpl implements IAdviceService {

    /**
     * 意见建议Mapper
     */
    private final AdviceMapper adviceMapper;

    /**
     * 构造方法注入
     *
     * @param adviceMapper 意见建议Mapper
     */
    @Autowired
    public AdviceServiceImpl(AdviceMapper adviceMapper) {
        // 赋值
        this.adviceMapper = adviceMapper;
    }

    /**
     * 查询意见建议列表
     *
     * @param advice 意见建议
     * @return 意见建议集合
     */
    @Override
    public List<AdviceVo> selectAdviceList(AdviceVo advice) {
        // 返回意见建议列表数据
        return adviceMapper.selectAdviceList(advice);
    }

    /**
     * 新增意见建议
     *
     * @param advice 意见建议
     * @return 结果
     */
    @Override
    public int insertAdvice(AdviceVo advice) {
        // 设置创建基础字段
        ParameterUtil.setCreateEntity(advice);
        // 设置用户id
        advice.setUserId(SecurityUtils.getUserId());
        // 设置插入条数
        return adviceMapper.insertAdvice(advice);
    }

}
