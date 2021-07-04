package com.ruoyi.project.storage.domain;

import com.ruoyi.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 积分记录对象
 *
 * @author yanjiaqi
 * @date 2020.05.02
 */
@Getter
@Setter
@ApiModel(value = "积分记录对象", description = "积分记录对象")
public class PointRecord extends BaseEntity {

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
     * 方式（1：注册赠送；2：活动获得；3：积分使用）
     */
    @ApiModelProperty(value = "方式")
    private Integer way;

    /**
     * 积分（积分获得为正数，积分使用未负数）
     */
    @ApiModelProperty(value = "积分")
    private Long points;

    /**
     * 广告id
     */
    @ApiModelProperty(value = "广告id")
    private Long advertisementId;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

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
    @ApiModelProperty(value = "删除标记")
    private String delFlag;

}
