package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.project.common.enums.RecordPointWay;
import com.ruoyi.project.common.enums.TerminalEnum;
import com.ruoyi.project.common.util.ParameterUtil;
import com.ruoyi.project.storage.domain.User;
import com.ruoyi.project.storage.mapper.UserMapper;
import com.ruoyi.project.storage.service.IPointRecordService;
import com.ruoyi.project.storage.service.IUserService;
import com.ruoyi.project.system.domain.SysUserRole;
import com.ruoyi.project.system.mapper.SysUserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户Service业务层处理
 *
 * @author yanjiaqi
 * @date 2020.04.28
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    /**
     * 积分记录Service
     */
    private final IPointRecordService pointRecordService;

    /**
     * 令牌Service
     */
    private final TokenService tokenService;

    /**
     * 用户Mapper
     */
    private final UserMapper userMapper;

    /**
     * 角色关联Mapper
     */
    private final SysUserRoleMapper userRoleMapper;

    /**
     * 构造方法注入
     *
     * @param tokenService       令牌 Service
     * @param pointRecordService 积分记录Service
     * @param userMapper         用户Mapper
     * @param userRoleMapper     角色关联Mapper
     */
    public UserServiceImpl(TokenService tokenService, IPointRecordService pointRecordService, UserMapper userMapper,
                           SysUserRoleMapper userRoleMapper) {
        // 赋值
        this.tokenService = tokenService;
        // 赋值
        this.pointRecordService = pointRecordService;
        // 赋值
        this.userMapper = userMapper;
        // 赋值
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * 查询用户列表
     *
     * @param user 用户
     * @return 用户集合
     */
    @Override
    public List<User> selectUserList(TerminalEnum terminalEnum, User user) {
        // 根据终端设置用户类型
        user.setUserType(terminalEnum.getType());
        // 返回用户列表数据
        return userMapper.selectUserList(user);
    }

    /**
     * 新增用户和手机端注册
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(TerminalEnum terminalEnum, User user) {
        // 如果为手机端注册
        if (TerminalEnum.REGIST.equals(terminalEnum)) {
            // 如果用户输入密码为空
            if (user.getPassword() == null) {
                // 手动抛异常
                throw new CustomException("请填写密码");
            }
            // 设置注册基础字段
            ParameterUtil.setRegistEntity(user);
        } else {
            // 设置创建基础字段
            ParameterUtil.setCreateEntity(user);
        }
        // 根据终端，设置部门id
        user.setDeptId(terminalEnum.getDept());
        // 根据终端，设置用户类型
        user.setUserType(terminalEnum.getType());
        // 根据终端，设置加密后的用户初始密码
        user.setPassword(SecurityUtils.encryptPassword("".equals(terminalEnum.getPassword()) ? user.getPassword() : terminalEnum.getPassword()));
        // 设置启用
        user.setStatus("0");
        // 根据终端，设置当前积分
        user.setCurrentPoints(terminalEnum.getPoint());
        // 插入用户返回条数
        int userCount = userMapper.insertUser(user);
        // 结果大于0则继续
        if (userCount > 0) {
            // 如果为注册，则插入积分记录表
            if (TerminalEnum.REGIST.equals(terminalEnum)) {
                // 插入积分记录表并返回条数
                userCount = pointRecordService.insertPointRecord(RecordPointWay.REGIST, user.getUserId(),
                        terminalEnum.getPoint(), null, null);
            }
            // 结果大于0则继续
            if (userCount > 0) {
                // 实例化List<SysUserRole>
                List<SysUserRole> list = new ArrayList<>();
                // 实例化SysUserRole
                SysUserRole sysUserRole = new SysUserRole();
                // 设置用户id
                sysUserRole.setUserId(user.getUserId());
                // 设置角色id
                sysUserRole.setRoleId(terminalEnum.getRole());
                // list赋值
                list.add(sysUserRole);
                // 插入用户角色关联表并返回插入条数
                return userRoleMapper.batchUserRole(list);
            } else {
                // 手动抛异常
                throw new CustomException("插入积分记录表失败");
            }
        } else {
            // 手动抛异常
            throw new CustomException("插入用户表失败");
        }
    }

    /**
     * 编辑用户
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(TerminalEnum terminalEnum, User user) {
        // 设置更新基础字段
        ParameterUtil.setUpdateEntity(user);
        // 根据终端，设置用户类型
        user.setUserType(terminalEnum.getType());
        // 修改条数
        int count = userMapper.updateUser(user);
        // 乐观锁判断
        if (count == 0) {
            // 打log
            log.error("UserServiceImpl.updateUser failed: 乐观锁");
            // 抛出异常标记乐观锁
            throw new CustomException("用户【" + user.getUserName() + "】已被他人率先修改，请刷新后重试", HttpStatus.CONFLICT);
        }
        // 返回修改数据
        return count;
    }

    /**
     * 批量删除用户
     *
     * @param userIds 需要删除的用户id数组
     * @return 结果
     */
    @Override
    public int deleteUserByIds(TerminalEnum terminalEnum, Long[] userIds) {
        // 返回修改条数
        return userMapper.deleteUserByIds(ParameterUtil.getBatchUpdateMapByIds(terminalEnum, userIds));
    }

    /**
     * 批量重置用户密码
     *
     * @param userIds 需要重置的用户id数组
     * @return 结果
     */
    @Override
    public int resetPwdUserByIds(TerminalEnum terminalEnum, Long[] userIds) {
        return userMapper.resetPwdUserByIds(ParameterUtil.getBatchResetMapByUserIds(terminalEnum, userIds));
    }

    /**
     * 批量启用/停用用户
     *
     * @param operate 操作类型（“enable”：启用； “disable”：停用）
     * @param userIds 需要启用/停用的客户ID数组
     * @return 结果
     */
    @Override
    public int operateUserByIds(TerminalEnum terminalEnum, String operate, Long[] userIds) {
        // 返回修改条数
        return userMapper.operateUserByIds(ParameterUtil.getBatchUpdateMapByOperateUserIds(terminalEnum, operate, userIds));
    }

    /**
     * 修改密码
     *
     * @param terminalEnum 枚举类型
     * @param oldPassword  旧密码
     * @param newPassword  新密码
     * @return 返回操作结果
     */
    @Override
    public int updateUserPassword(TerminalEnum terminalEnum, String oldPassword, String newPassword) {
        // 获取当前用户身份信息
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 获取用户账号
        String userName = loginUser.getUsername();
        // 获取用户当前密码
        String password = loginUser.getPassword();
        // 判断旧密码是否正确
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            // 返回错误提示
            throw new CustomException("修改密码失败，旧密码错误", HttpStatus.ERROR);
        }
        // 判断新旧密码是否相同
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            // 返回错误提示
            throw new CustomException("新密码不能与旧密码相同", HttpStatus.ERROR);
        }
        // 定义更新参数
        User user = new User();
        // 设置用户名
        user.setUserName(userName);
        // 设置密码
        user.setPassword(SecurityUtils.encryptPassword(newPassword));
        // 设置用户类型
        user.setUserType(terminalEnum.getType());
        // 设置更新基础字段
        ParameterUtil.setUpdateEntity(user);
        // 判断更新密码结果
        if (userMapper.updateUserPassword(user) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            // 设置用户身份信息
            tokenService.setLoginUser(loginUser);
            // 返回操作结果
            return 1;
        }
        throw new CustomException("修改密码异常，请联系管理员", HttpStatus.ERROR);
    }

    /**
     * 获取用户总积分值
     *
     * @param userId 用户id
     * @return 当前用户积分
     */
    @Override
    public long getCurrentPoints(Long userId) {
        // 获取当前用户积分
        Long userPoints = userMapper.getCurrentPoints(SecurityUtils.getUserId());
        // 返回当前用户积分
        return userPoints == null ? 0L : userPoints;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(TerminalEnum terminalEnum, String userName) {
        // 查看用户名是否唯一
        int count = userMapper.checkUserNameUnique(userName);
        // 如果数据库中已存在该用户名
        if (count > 0) {
            // 返回不唯一标识
            return UserConstants.NOT_UNIQUE;
        }
        // 返回唯一标识
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public String checkPhoneUnique(TerminalEnum terminalEnum, User user) {
        // 判断用户id是否为空
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        // 获取用户手机号码
        User info = userMapper.checkPhoneUnique(user.getPhonenumber());
        // 判断用户手机号码是否为空且用户id是否已经存在
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            // 返回不唯一标识
            return UserConstants.NOT_UNIQUE;
        }
        // 返回唯一标识
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public String checkEmailUnique(TerminalEnum terminalEnum, User user) {
        // 判断用户id是否为空
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        // 获取用户邮箱号码
        User info = userMapper.checkEmailUnique(user.getEmail());
        // 判断用户手机号码是否为空且用户邮箱是否已经存在
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            // 返回不唯一标识
            return UserConstants.NOT_UNIQUE;
        }
        // 返回唯一标识
        return UserConstants.UNIQUE;
    }

}
