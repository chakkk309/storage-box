package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 用户管理接口Mapper
 *
 * @author yanjiaqi
 * @date 2020.05.01
 */
public interface UserMapper {

    /**
     * 查询用户列表
     *
     * @param user 用户
     * @return 用户集合
     */
    List<User> selectUserList(User user);

    /**
     * 新增用户列表
     *
     * @param user 用户
     * @return 用户集合
     */
    int insertUser(User user);

    /**
     * 编辑用户列表
     *
     * @param user 用户
     * @return 用户集合
     */
    int updateUser(User user);

    /**
     * 批量删除用户
     *
     * @param map userId：需要删除的用户ID
     *            user：操作人
     *            time：操作时间
     * @return 修改条数
     */
    int deleteUserByIds(Map<String, Object> map);

    /**
     * 重置用户密码
     *
     * @param map terminalEnum:终端类型（APP：手机端；BACKEND：后台端；SYSTEM：系统端；REGIST：手机注册端）
     *            userIds:需要充值密码的用户id
     * @return 修改条数
     */
    int resetPwdUserByIds(Map<String, Object> map);

    /**
     * 更新用户密码
     *
     * @param user 用户信息
     * @return 修改条数
     */
    int updateUserPassword(User user);

    /**
     * 批量启用/停用用户
     *
     * @param map userId：需要启用的用户ID
     *            operate：操作类型（“enable”：启用；“disable”：停用）
     *            user：操作人
     *            time：操作时间
     * @return 修改条数
     */
    int operateUserByIds(Map<String, Object> map);

    /**
     * 获取当前用户积分
     *
     * @param userId 用户id
     * @return 当前用户积分
     */
    long getCurrentPoints(Long userId);

    /**
     * 操作用户积分
     *
     * @param user 用户信息
     * @return 修改条数
     */
    int operateUserPoints(User user);

    /**
     * 通过用户id更新用积分
     *
     * @param user 用户
     * @return 修改条数
     */
    int updateUserPoints(User user);

    /**
     * 通过用户id获取用户信息
     *
     * @param userId 当前用户id
     * @return 用户信息
     */
    User selectUserByUserId(Long userId);

    /**
     * 通过用户id查询当前积分
     *
     * @param userId 用户id
     * @return 修改条数
     */
    Long selectUserPointByUserId(Long userId);

    /**
     * 通过用户id获取用户类型（01： 后台端用户；02：手机端用户）
     *
     * @param userId 用户id
     * @return 用户类型
     */
    String selectUserTypeByUserId(Long userId);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 修改条数
     */
    int checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phoneNumber 手机号码
     * @return 修改条数
     */
    User checkPhoneUnique(String phoneNumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 修改条数
     */
    User checkEmailUnique(String email);

}
