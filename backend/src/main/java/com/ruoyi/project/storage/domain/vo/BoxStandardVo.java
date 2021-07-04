package com.ruoyi.project.storage.domain.vo;

import com.ruoyi.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 箱子规格对象
 *
 * @author yanjiaqi
 * @date 2020.05.03
 */
@Getter
@Setter
@ApiModel(value = "箱子规格对象", description = " 箱子规格对象")
public class BoxStandardVo extends BaseEntity {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 箱子规格（如20*20*20）
     */
    @ApiModelProperty(value = "箱子规格")
    private String boxStandard;

    /**
     * 箱子积分单价（每月积分单价）
     */
    @ApiModelProperty(value = "箱子积分单价")
    private Long boxUnitPrice;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Long sortNo;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private Long version;

    /**
     * 删除标记（0：未删除；2：已删除）
     */
    @ApiModelProperty(value = "删除标记（0：未删除；2：已删除）")
    private String delFlag;

    /**
     * 总数量 (关联箱子信息表计算得出)
     */
    @ApiModelProperty(value = "总数量")
    private Long totalNumber;

    /**
     * 已使用数量 (关联箱子信息表计算得出)
     */
    @ApiModelProperty(value = "已使用数量")
    private Long usedNumber;

    /**
     * 库存数量 (关联箱子信息表计算得出)
     */
    @ApiModelProperty(value = "库存数量")
    private Long inventoryNumber;

    /**
     * 使用比例 (关联箱子信息表计算得出)
     */
    @ApiModelProperty(value = "使用比例")
    private String useRatio;

}
