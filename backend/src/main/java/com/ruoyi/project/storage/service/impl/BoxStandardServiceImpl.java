package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.common.util.ParameterUtil;
import com.ruoyi.project.common.util.SeqGeneratorUtil;
import com.ruoyi.project.storage.domain.vo.BoxInfoVo;
import com.ruoyi.project.storage.domain.vo.BoxStandardSelectVo;
import com.ruoyi.project.storage.domain.vo.BoxStandardVo;
import com.ruoyi.project.storage.mapper.BoxInfoMapper;
import com.ruoyi.project.storage.mapper.BoxStandardMapper;
import com.ruoyi.project.storage.service.IBoxStandardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 箱子规格Service业务层处理
 *
 * @author yanjiaqi
 * @date 2020.04.28
 */
@Service
@Slf4j
public class BoxStandardServiceImpl implements IBoxStandardService {

    /**
     * 箱子信息Mapper
     */
    private final BoxInfoMapper boxInfoMapper;

    /**
     * 箱子规格Mapper
     */
    private final BoxStandardMapper boxStandardMapper;

    /**
     * 构造方法注入
     *
     * @param boxStandardMapper 箱子规格Mapper
     */
    @Autowired
    public BoxStandardServiceImpl(BoxStandardMapper boxStandardMapper, BoxInfoMapper boxInfoMapper) {
        // 赋值
        this.boxStandardMapper = boxStandardMapper;
        // 赋值
        this.boxInfoMapper = boxInfoMapper;
    }

    /**
     * 查询箱子规格列表
     *
     * @param boxStandardVo 箱子规格
     * @return 箱子规格集合
     */
    @Override
    public List<BoxStandardVo> selectBoxStandardVoList(BoxStandardVo boxStandardVo) {
        // 返回查询结果
        return boxStandardMapper.selectBoxStandardList(boxStandardVo);
    }

    /**
     * 新增箱子规格
     *
     * @param boxStandardVo 箱子规格
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBoxStandard(BoxStandardVo boxStandardVo) {
        // 通过箱子规格查询箱子规格信息
        BoxStandardSelectVo boxStandardSelectVo = boxStandardMapper.selectBoxStandardByStandard(boxStandardVo);
        // 判断查询到的箱子规格信息
        if (boxStandardSelectVo != null) {
            // 箱子积分单价一定不为空
            if (!boxStandardVo.getBoxUnitPrice().equals(boxStandardSelectVo.getBoxUnitPrice())) {
                // 若积分单价不同，则手动抛异常
                throw new CustomException("当前规格 【" + boxStandardVo.getBoxStandard() + "】已存在，对应所需积分为【" + boxStandardSelectVo.getBoxUnitPrice() + "】");
            }
            // 判断传递过来的库存是否小于等于0
            if (boxStandardVo.getInventoryNumber() <= 0L) {
                throw new CustomException("当前规格 【" + boxStandardVo.getBoxStandard() + "】已存在，本次无新增库存，无需重新新增");
            }
        } else {
            // 设置创建基础字段
            ParameterUtil.setCreateEntity(boxStandardVo);
            // 新增箱子规格
            int insertCount = boxStandardMapper.insertBoxStandard(boxStandardVo);
            // 判断插入箱子规格结果
            if (insertCount <= 0) {
                // 插入失败，抛异常
                throw new CustomException("箱子规格插入失败，请联系管理员");
            }
            // 判断传递过来的库存数量是否小于等于0
            if (boxStandardVo.getInventoryNumber() <= 0L) {
                // 直接返回插入条数
                return insertCount;
            }
        }
        // 返回批量插入箱子信息条数
        return boxInfoMapper.batchInsertBoxInfo(initBoxInfoList(boxStandardVo));
    }

    /**
     * 根据箱子规格显示对象，初始化箱子信息List
     *
     * @param boxStandardVo 箱子规格对象
     * @return 结果
     */
    @Override
    public List<BoxInfoVo> initBoxInfoList(BoxStandardVo boxStandardVo) {
        // 获取当前时间
        Date date = DateUtils.getNowDate();
        // 获取当前登录人用户名
        String userName = SecurityUtils.getUsername();
        // 初始化箱子信息List
        List<BoxInfoVo> list = new ArrayList<>();
        // 遍历 （遍历库存数量必大于0）
        for (int i = 0; i < boxStandardVo.getInventoryNumber(); i++) {
            // 实例化箱子信息对象
            BoxInfoVo boxInfo = new BoxInfoVo();
            // 设置箱子编号，年月日+6位序列（工具生成）
            boxInfo.setBoxCode(Long.valueOf(SeqGeneratorUtil.seqGenerator(DateUtils.getNowDateStr(), 6)));
            // 设置箱子规格
            boxInfo.setBoxStandard(boxStandardVo.getBoxStandard());
            // 设置箱子积分单价
            boxInfo.setBoxUnitPrice(boxStandardVo.getBoxUnitPrice());
            // 设置未使用
            boxInfo.setIsUsed(0);
            // 设置备注
            boxInfo.setRemark(boxStandardVo.getRemark());
            // 设置创建人
            boxInfo.setCreateBy(userName);
            // 设置创建时间
            boxInfo.setCreateTime(date);
            // 设置更新人
            boxInfo.setUpdateBy(userName);
            // 设置更新时间
            boxInfo.setUpdateTime(date);
            // 设置版本号
            boxInfo.setVersion(0L);
            // 设置删除标记（未删除)
            boxInfo.setDelFlag("0");
            // 箱子实例添加到箱子信息中
            list.add(boxInfo);
        }
        // 返回箱子信息List
        return list;
    }

    /**
     * 批量删除箱子规格
     *
     * @param ids 需要删除的箱子规格ID数组
     * @return 结果
     */
    @Override
    public int deleteBoxStandardByIds(Long[] ids) {
        // 判断该规格下有无箱子
        if (boxStandardMapper.selectBoxInfoCountByIds(ids) > 0) {
            // 若箱子规格id对应规格下仍有箱子，手动抛异常
            throw new CustomException("删除箱子规格失败，该规格下仍有箱子");
        }
        // 返回修改条数
        return boxStandardMapper.deleteBoxStandardByIds(ParameterUtil.getBatchUpdateMapByIds(ids));
    }

    /**
     * 箱子规格下拉列表
     *
     * @return 结果
     */
    @Override
    public List<BoxStandardSelectVo> selectBoxStandard() {
        // 返回修改条数
        return boxStandardMapper.selectBoxStandard();
    }

}
