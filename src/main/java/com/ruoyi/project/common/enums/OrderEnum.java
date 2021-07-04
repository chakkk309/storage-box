package com.ruoyi.project.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态
 *
 * @author yanjiaqi
 * @date 2020.05.13
 */
@AllArgsConstructor
@Getter
public enum OrderEnum {

    /**
     * 后台端 2：【发送空箱】
     */
    BACKEND_SEND_EMPTY_BOX("01", 2),

    /**
     * 手机端 3：【收取空箱】
     */
    APP_GET_EMPTY_BOX("02", 3),

    /**
     * 手机端 4：【发送重箱】
     */
    APP_SEND_HEAVY_BOX("02", 4),

    /**
     * 后台端 5：【收取重箱】
     */
    BACKEND_CALL_HEAVY_BOX("01", 5),

    /**
     * 手机端 6：【预约提取】
     */
    APP_ORDER_CALL("02", 6),

    /**
     * 后台端 7：【发送重箱】
     */
    BACKEND_SEND_HEAVY_BOX("01", 7),

    /**
     * 手机端 8：【收取重箱】
     */
    APP_CALL_HEAVY_BOX("02", 8),

    /**
     * 手机端 9：【发送空箱】
     */
    APP_SEND_EMPTY_BOX("02", 9),

    /**
     * 后台端 10：【收取空箱】
     */
    BACKEND_CALL_EMPTY_BOX("01", 10),

    /**
     * 手机端 10：【删除订单】
     */
    APP_DELETE_ORDER("02", 10);

    /**
     * 类型
     */
    private String terminal;

    /**
     * 操作
     */
    private Integer operate;

}
