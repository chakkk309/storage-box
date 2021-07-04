package com.ruoyi.project.storage.domain.vo;

import com.ruoyi.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 箱子信息对象
 *
 * @author yanjiaqi
 * @date 2020.05.09
 */
@Getter
@Setter
@ApiModel(value = "箱子信息对象", description = " 箱子信息对象")
public class BoxInfoVo extends BaseEntity {

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
     * 箱子编号（年月日+6位序列）
     */
    @ApiModelProperty(value = "箱子编号")
    private Long boxCode;

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
     * 使用人（用户id）
     */
    @ApiModelProperty(value = "使用人")
    private Long usedBy;

    /**
     * 使用人姓名（用户名称）通过关联表sys_user中手机端用户的userId得到
     */
    @ApiModelProperty(value = "使用人姓名")
    private String usedByName;

    /**
     * 是否被使用（0：未使用；1：已使用）
     */
    @ApiModelProperty(value = "是否被使用")
    private Integer isUsed;

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

}
