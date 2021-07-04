package com.ruoyi.project.common.util;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.domain.BaseEntity;
import com.ruoyi.project.common.enums.TerminalEnum;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 参数工具类
 *
 * @author wangdong
 * @date 2020.04.28
 *
 * 版本        修改时间        作者        修改内容
 * V1.0        2020-04-28     wangdong    初始版本
 * V1.1        2020-05-22     yanjiaqi    通过传入订单id、操作类型及版本号返回相应集合、通过传入枚举类型、id数组返回相应集合
 */
@Service
public class ParameterUtil {

    /**
     * 通过传入id数组返回Map<String, Object>
     *
     * @param ids 传入id数组
     * @return Map
     */
    public static Map<String, Object> getBatchUpdateMapByIds(Long[] ids) {
        // 初始化map
        Map<String, Object> map = initMap();
        // 设置id数组
        map.put("ids", ids);
        // 返回map
        return map;
    }

    /**
     * 通过传入地址id和用户id数组返回Map<String, Object>
     *
     * @param ids    传入id数组
     * @param userId 用户id
     * @return Map
     */
    public static Map<String, Object> getBatchUpdateMapByIds(Long[] ids, Long userId) {
        // 初始化map
        Map<String, Object> map = initMap();
        // 设置id数组
        map.put("ids", ids);
        //设置用户id
        map.put("userId", userId);
        // 返回map
        return map;
    }

    /**
     * 通过传入操作类型和id数组返回Map<String, Object>
     *
     * @param operate 操作类型（"enable"：启用；"disable"：停用）
     * @param ids     传入id数组
     * @return Map
     */
    public static Map<String, Object> getBatchUpdateMapByOperateIds(String operate, Long[] ids) {
        // 初始化map
        Map<String, Object> map = initMap();
        // 设置操作类型
        map.put("operate", operate);
        // 设置id数组
        map.put("ids", ids);
        // 返回map
        return map;
    }

    /**
     * 通过传入枚举类型、id数组返回Map<String, Object>
     *
     * @param terminalEnum 枚举类型
     * @param ids          传入id数组
     * @return Map
     */
    public static Map<String, Object> getBatchUpdateMapByIds(TerminalEnum terminalEnum, Long[] ids) {
        // 初始化map
        Map<String, Object> map = initMap();
        // 设置id数组
        map.put("ids", ids);
        // 返回map
        return map;
    }

    /**
     * 通过传入userId数组返回重置密码Map<String, Object>
     *
     * @param terminalEnum 终端类型（APP：手机端；BACKEND：后台端；SYSTEM：系统端；REGIST：手机注册端）
     * @param userIds      传入userId数组
     * @return Map
     */
    public static Map<String, Object> getBatchResetMapByUserIds(TerminalEnum terminalEnum, Long[] userIds) {
        // 初始化map
        Map<String, Object> map = initMap();
        // 设置id数组
        map.put("userIds", userIds);
        // 设置用户类型
        map.put("userType", terminalEnum.getType());
        // 设置用户密码
        map.put("password", SecurityUtils.encryptPassword(terminalEnum.getPassword()));
        // 返回map
        return map;
    }

    /**
     * 通过传入userId数组及操作返回Map<String, Object>
     *
     * @param terminalEnum 终端类型（APP：手机端；BACKEND：后台端；SYSTEM：系统端；REGIST：手机注册端）
     * @param operate      操作类型（"enable"：启用；"disable"：停用）
     * @param userIds      传入userId数组
     * @return Map
     */
    public static Map<String, Object> getBatchUpdateMapByOperateUserIds(TerminalEnum terminalEnum, String operate, Long[] userIds) {
        // 初始化map
        Map<String, Object> map = initMap();
        // 设置操作类型
        map.put("operate", operate);
        // 设置id数组
        map.put("userIds", userIds);
        // 设置用户类型
        map.put("userType", terminalEnum.getType());
        // 返回map
        return map;
    }


    /**
     * 通过传入订单id、操作类型及版本号返回Map<String, Object>
     *
     * @param id       订单id
     * @param operate  操作类型
     * @param version  版本号
     * @param userType 用户类型
     * @param userId   当前订单用户id
     * @return Map
     */
    public static Map<String, Object> getBatchUpdateMap(Long id, Integer operate, Long version, String userType, Long userId) {
        // 初始化map
        Map<String, Object> map = initMap();
        // 设置id数组
        map.put("id", id);
        // 设置操作类型
        map.put("operate", operate);
        // 设置版本号
        map.put("version", version);
        // 设置用户类型
        map.put("userType", userType);
        // 设置当前订单用户的id
        map.put("UserIdById", userId);
        // 返回map
        return map;
    }

    /**
     * 设置注册基础字段
     *
     * @param t 泛型
     */
    public static <T extends BaseEntity> void setRegistEntity(T t) {
        // 获取当前时间
        Date date = DateUtils.getNowDate();
        // 获取当前登录人用户名
        String userName = "appRegist";
        // 设置更新时间
        t.setUpdateTime(date);
        // 设置更新人
        t.setUpdateBy(userName);
        // 设置创建时间
        t.setCreateTime(date);
        // 设置创建人
        t.setCreateBy(userName);
        // 设置版本号
        t.setVersion(0L);
        // 设置未删除
        t.setDelFlag("0");
    }

    /**
     * 设置创建基础字段
     *
     * @param t 泛型
     */
    public static <T extends BaseEntity> void setCreateEntity(T t) {
        // 获取当前时间
        Date date = DateUtils.getNowDate();
        // 获取当前登录人用户名
        String userName = SecurityUtils.getUsername();
        // 设置创建时间
        t.setCreateTime(date);
        // 设置创建人
        t.setCreateBy(userName);
        // 设置更新时间
        t.setUpdateTime(date);
        // 设置更新人
        t.setUpdateBy(userName);
        // 设置版本号
        t.setVersion(0L);
        // 设置未删除
        t.setDelFlag("0");
    }

    /**
     * 设置更新基础字段
     *
     * @param t 泛型
     */
    public static <T extends BaseEntity> void setUpdateEntity(T t) {
        // 获取当前时间
        Date date = DateUtils.getNowDate();
        // 获取当前登录人用户名
        String userName = SecurityUtils.getUsername();
        // 设置更新时间
        t.setUpdateTime(date);
        // 设置更新人
        t.setUpdateBy(userName);
    }

    /**
     * 初始化返回参数Map<String, Object>
     *
     * @return 返回参数
     */
    private static Map<String, Object> initMap() {
        // 定义Map
        Map<String, Object> map = new HashMap<>(16);
        // 设置更新时间
        map.put("time", DateUtils.getNowDate());
        // 设置更新人
        map.put("user", SecurityUtils.getUsername());
        // 设置用户id
        map.put("userId", SecurityUtils.getUserId());
        // 返回参数
        return map;
    }

}
