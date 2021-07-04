package com.ruoyi.project.storage.service;

import com.ruoyi.project.common.enums.TerminalEnum;
import com.ruoyi.project.storage.domain.User;

import java.util.List;

/**
 * 用户Service
 *
 * @author yanjiaqi
 * @date 2020.05.05
 */
public interface IUserService {

    /**
     * 查询用户列表
     *
     * @param terminalEnum 终端类型 (APP：手机端、 BACKEND：后台端、SYSTEM：系统端）
     * @param user         用户
     * @return 用户信息集合
     */
    List<User> selectUserList(TerminalEnum terminalEnum, User user);

    /**
     * 新增用户
     *
     * @param terminalEnum 终端类型 (APP：手机端、 BACKEND：后台端、SYSTEM：系统端）
     * @param user         用户
     * @return 修改条数
     */
    int insertUser(TerminalEnum terminalEnum, User user);

    /**
     * 编辑用户
     *
     * @param terminalEnum 终端类型 (APP：手机端、 BACKEND：后台端、SYSTEM：系统端）
     * @param user         用户
     * @return 修改条数
     */
    int updateUser(TerminalEnum terminalEnum, User user);

    /**
     * 删除用户
     *
     * @param terminalEnum 终端类型 (APP：手机端、 BACKEND：后台端、SYSTEM：系统端）
     * @param userIds      需要删除的用户ID数组
     * @return 修改条数
     */
    int deleteUserByIds(TerminalEnum terminalEnum, Long[] userIds);

    /**
     * 重置用户密码
     *
     * @param terminalEnum 终端类型 (APP：手机端、 BACKEND：后台端、SYSTEM：系统端）
     * @param userIds      需要重置的用户ID数组
     * @return 修改条数
     */
    int resetPwdUserByIds(TerminalEnum terminalEnum, Long[] userIds);

    /**
     * 批量启用/停用用户
     *
     * @param terminalEnum 终端类型 (APP：手机端、 BACKEND：后台端、SYSTEM：系统端）
     * @param operate      操作类型（“enable”：启用； “disable”：停用）
     * @param userIds      需要启用/停用的客户ID数组
     * @return 修改条数
     */
    int operateUserByIds(TerminalEnum terminalEnum, String operate, Long[] userIds);

    /**
     * 修改密码
     *
     * @param terminalEnum 终端类型 (APP：手机端、 BACKEND：后台端、SYSTEM：系统端）
     * @param oldPassword  旧密码
     * @param newPassword  新密码
     * @return 修改条数
     */
    int updateUserPassword(TerminalEnum terminalEnum, String oldPassword, String newPassword);

    /**
     * 获取用户总积分值
     *
     * @param userId 用户id
     * @return 用户总积分值
     */
    long getCurrentPoints(Long userId);

    /**
     * 校验用户名称是否唯一
     *
     * @param terminalEnum 终端类型 (APP：手机端、 BACKEND：后台端、SYSTEM：系统端）
     * @param userName     用户名称
     * @return 修改条数
     */
    String checkUserNameUnique(TerminalEnum terminalEnum, String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param terminalEnum 终端类型 (APP：手机端、 BACKEND：后台端、SYSTEM：系统端）
     * @param user         用户信息
     * @return 修改条数
     */
    String checkPhoneUnique(TerminalEnum terminalEnum, User user);

    /**
     * 校验email是否唯一
     *
     * @param terminalEnum 终端类型 (APP：手机端、 BACKEND：后台端、SYSTEM：系统端）
     * @param user         用户信息
     * @return 修改条数
     */
    String checkEmailUnique(TerminalEnum terminalEnum, User user);

}
