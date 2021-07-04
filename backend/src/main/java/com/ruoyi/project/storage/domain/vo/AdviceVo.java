package com.ruoyi.project.storage.domain.vo;

import com.ruoyi.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 意见建议对象
 *
 * @author yanjiaqi
 * @date 2020.04.29
 */
@Getter
@Setter
@ApiModel(value = "意见建议对象", description = "意见建议对象")
public class AdviceVo extends BaseEntity {

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
     * 手机端用户id，与sys_user表中user_id字段相对应
     */
    @ApiModelProperty(value = "手机端用户id")
    private Long userId;

    /**
     * 姓名，sys_user表中的nick_name字段
     */
    @ApiModelProperty(value = "姓名")
    private String nickName;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

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
