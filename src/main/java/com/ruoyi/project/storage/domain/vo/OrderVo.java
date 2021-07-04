package com.ruoyi.project.storage.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 订单对象
 *
 * @author yanjiaqi
 * @date 2020.05.09
 */
@Getter
@Setter
@ApiModel(value = "订单列表对象", description = " 订单列表对象")
public class OrderVo extends BaseEntity {

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
     * 手机端用户id
     */
    @ApiModelProperty(value = "手机端用户id")
    private Long userId;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String nickName;

    /**
     * 后台端对状态的解读
     */
    @ApiModelProperty(value = "后台端对状态的解读")
    private Integer status;

    /**
     * 订单编号（年月日+6位序列）
     */
    @ApiModelProperty(value = "订单编号（年月日+6位序列）")
    private Long orderCode;

    /**
     * 订单名称
     */
    @ApiModelProperty(value = "订单名称")
    private String orderName;

    /**
     * 空箱上门下单时间（订单状态为1-5，显示）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "空箱上门下单时间（订单状态为1-5，显示）")
    private Date emptyBoxOrderTime;

    /**
     * 重箱提取下单时间（订单状态为6-10，显示）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "重箱提取下单时间（订单状态为6-10，显示）")
    private Date heavyBoxOrderTime;

    /**
     * 空箱上门姓名（订单状态为1-5，显示）
     */
    @ApiModelProperty(value = "空箱上门姓名（订单状态为1-5，显示）")
    private String emptyBoxCallName;

    /**
     * 空箱上门电话（订单状态为1-5，显示）
     */
    @ApiModelProperty(value = "空箱上门电话（订单状态为1-5，显示）")
    private String emptyBoxCallPhone;

    /**
     * 空箱上门地址（订单状态为1-5，显示）
     */
    @ApiModelProperty(value = "空箱上门地址（订单状态为1-5，显示）")
    private String emptyBoxCallAddress;

    /**
     * 空箱上门时间（年月日，订单状态为1-5，显示）
     */
    @ApiModelProperty(value = "空箱上门时间（年月日，订单状态为1-5，显示）")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date emptyBoxCallTime;

    /**
     * 空箱上门时段（如09:00-13:00，订单状态为1-5，显示）
     */
    @ApiModelProperty(value = "空箱上门时段（如09:00-13:00，订单状态为1-5，显示）")
    private String emptyBoxCallInterval;

    /**
     * 重箱提取姓名（订单状态为6-10，显示）
     */
    @ApiModelProperty(value = "重箱提取姓名（订单状态为6-10，显示）")
    private String heavyBoxCallName;

    /**
     * 重箱提取电话（订单状态为6-10，显示）
     */
    @ApiModelProperty(value = "重箱提取电话（订单状态为6-10，显示）")
    private String heavyBoxCallPhone;

    /**
     * 重箱提取地址（订单状态为6-10，显示）
     */
    @ApiModelProperty(value = "重箱提取地址（订单状态为6-10，显示）")
    private String heavyBoxCallAddress;

    /**
     * 重箱提取时间（年月日，订单状态为6-10，显示）
     */
    @ApiModelProperty(value = "重箱提取时间（年月日，订单状态为6-10，显示）")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date heavyBoxCallTime;

    /**
     * 重箱提取时段（如09:00-13:00，订单状态为6-10，显示）
     */
    @ApiModelProperty(value = "重箱提取时段（如09:00-13:00，订单状态为6-10，显示）")
    private String heavyBoxCallInterval;

    /**
     * 箱子id
     */
    @ApiModelProperty(value = "箱子id")
    private Long boxId;

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
     * 租赁时长（月）
     */
    @ApiModelProperty(value = "租赁时长（月）")
    private Integer leaseDuration;

    /**
     * 实付积分
     */
    @ApiModelProperty(value = "实付积分")
    private Long totalPrice;

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
     * 手机端删除标记（0：未删除；2：已删除
     */
    @ApiModelProperty(value = "手机端删除标记（0：未删除；2：已删除")
    private Integer appDelFlag;

    /**
     * 后台端删除标记（0：未删除；2：已删除）
     */
    @ApiModelProperty(value = "后台端删除标记（0：未删除；2：已删除）")
    private Integer backendDelFlag;

    /**
     * 空箱上门下单开始时间
     */
    @ApiModelProperty(value = "空箱上门下单开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String emptyBoxOrderTimeStart;

    /**
     * 空箱上门下单结束时间
     */
    @ApiModelProperty(value = "空箱上门下单结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String emptyBoxOrderTimeEnd;

    /**
     * 重箱提取下单开始时间
     */
    @ApiModelProperty(value = "重箱提取下单开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String heavyBoxOrderTimeStart;

    /**
     * 重箱提取下单结束时间
     */
    @ApiModelProperty(value = "重箱提取下单结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String heavyBoxOrderTimeEnd;

}
