package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.common.util.ParameterUtil;
import com.ruoyi.project.storage.domain.vo.BoxInfoVo;
import com.ruoyi.project.storage.mapper.BoxInfoMapper;
import com.ruoyi.project.storage.service.IBoxInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.ruoyi.project.common.util.SeqGeneratorUtil.seqGenerator;

/**
 * 箱子信息Service业务层处理
 *
 * @author yanjiaqi
 * @date 2020.05.09
 */
@Service
@Slf4j
public class BoxInfoServiceImpl implements IBoxInfoService {

    /**
     * 箱子信息Mapper
     */
    private final BoxInfoMapper boxInfoMapper;

    /**
     * 构造方法注入
     *
     * @param boxInfoMapper 箱子信息Mapper
     */
    @Autowired
    public BoxInfoServiceImpl(BoxInfoMapper boxInfoMapper) {
        // 赋值
        this.boxInfoMapper = boxInfoMapper;
    }

    /**
     * 查询箱子信息列表
     *
     * @param boxInfo 箱子信息
     * @return 箱子信息集合
     */
    @Override
    public List<BoxInfoVo> selectBoxInfoVoList(BoxInfoVo boxInfo) {
        // 返回查询结果
        return boxInfoMapper.selectBoxInfoList(boxInfo);
    }

    /**
     * 新增箱子信息
     *
     * @param boxInfo 箱子信息
     * @return 结果
     */
    @Override
    public int insertBoxInfo(BoxInfoVo boxInfo) {
        // 设置基础字段
        ParameterUtil.setCreateEntity(boxInfo);
        // 设置箱子编号（年月日+6位序列）
        boxInfo.setBoxCode(Long.valueOf(seqGenerator(DateUtils.getNowDateStr(), 6)));
        // 设置是否被使用（0：未使用）
        boxInfo.setIsUsed(0);
        // 设置插入条数
        return boxInfoMapper.insertBoxInfo(boxInfo);
    }

    /**
     * 批量删除箱子信息
     *
     * @param ids 需要删除的箱子信息ID数组
     * @return 结果
     */
    @Override
    public int deleteBoxInfoByIds(Long[] ids) {
        // 判断当前箱子下仍有订单（未完成的订单)
        if (boxInfoMapper.selectBoxOrderCountByIds(ids) > 0) {
            // 若箱子信息id对应箱子下未完成订单，手动抛异常
            throw new CustomException("删除箱子信息失败，当前箱子下仍有订单未完成");
        }
        // 返回修改条数
        return boxInfoMapper.deleteBoxInfoByIds(ParameterUtil.getBatchUpdateMapByIds(ids));
    }

    /**
     * 查询具体规格下可用箱子信息
     *
     * @param boxStandard 箱子规格
     * @return 结果
     */
    @Override
    public List<BoxInfoVo> selectUsedBoxInfo(String boxStandard) {
        // 返回结果
        return boxInfoMapper.selectUsedBoxInfo(boxStandard);
    }

    /**
     * 更新箱子信息
     *
     * @param boxInfoVo 箱子信息
     * @return 结果
     */
    @Override
    public int updateBoxInfo(BoxInfoVo boxInfoVo) {
        // 返回结果
        return boxInfoMapper.updateBoxInfo(boxInfoVo);
    }

}
