package com.ruoyi.project.storage.domain.vo;

import com.ruoyi.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 箱子规格查询对象
 *
 * @author yanjiaqi
 * @date 2020.05.08
 */
@Getter
@Setter
@ApiModel(value = "箱子规格查询对象", description = " 箱子规格查询对象")
public class BoxStandardSelectVo extends BaseEntity {

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

}
