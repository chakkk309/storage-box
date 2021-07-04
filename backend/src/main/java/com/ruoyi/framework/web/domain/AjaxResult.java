package com.ruoyi.framework.web.domain;

import java.util.HashMap;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.utils.StringUtils;

/**
 * 通用返回实体（非分页）
 *
 * @author wangdong
 *
 * 版本         修改时间        作者        修改内容
 * V1.0        未知            wangdong    初始版本
 * V1.1        2020-5-21       yanjiaqi    创建返回通用错误消息和返回通用成功消息
 */
public class AjaxResult extends HashMap<String, Object> {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1L;

    /**
     * 消息状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 消息内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        // 返回成功消息
        return AjaxResult.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        // 返回成功消息
        return AjaxResult.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg) {
        // 返回成功消息
        return AjaxResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        // 返回成功消息
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return 错误提示
     */
    public static AjaxResult error() {
        // 返回失败提示
        return AjaxResult.error("操作失败");
    }

    /**
     * 返回通用成功消息
     *
     * @return 成功提示
     */
    public static AjaxResult commonSuccess(String operate) {
        // 返回成功提示
        return AjaxResult.success(operate + "成功");
    }

    /**
     * 返回通用错误消息
     *
     * @return 错误提示
     */
    public static AjaxResult commonError(String operate) {
        // 返回失败提示
        return AjaxResult.error(operate + "失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg) {
        // 返回失败提示
        return AjaxResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data) {
        // 返回失败提示
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static AjaxResult error(int code, String msg) {
        // 返回失败提示
        return new AjaxResult(code, msg, null);
    }

}