package com.ruoyi.project.system.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;

/**
 * 个人信息 业务处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile()
    {
        // 通过request请求获取用户身份信息
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 实例化user对象并获取用户身份信息
        SysUser user = loginUser.getUser();
        // 返回操作成功数据（msg，data）
        AjaxResult ajax = AjaxResult.success(user);
        // 建立角色映射
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        // 返回通用返回实体（非分页）
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user)
    {
        // 如果用户基本信息被修改
        if (userService.updateUserProfile(user) > 0)
        {
            // 获取用户身份信息
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            // 更新用户姓名
            loginUser.getUser().setNickName(user.getNickName());
            // 更新用户手机号
            loginUser.getUser().setPhonenumber(user.getPhonenumber());
            // 更新用户邮箱
            loginUser.getUser().setEmail(user.getEmail());
            // 更新用户性别
            loginUser.getUser().setSex(user.getSex());
            // 更新缓存用户信息
            tokenService.setLoginUser(loginUser);
            // 返回操作成功提示
            return AjaxResult.success();
        }
        // 返回操作失败提示
        return AjaxResult.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword)
    {
        // 获取用户身份信息
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 获取用户账号
        String userName = loginUser.getUsername();
        // 获取用户当前密码
        String password = loginUser.getPassword();
        // 旧密码输入错误
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            // 返回错误提示
            return AjaxResult.error("修改密码失败，旧密码错误");
        }
        // 新密码相同
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            // 返回错误提示
            return AjaxResult.error("新密码不能与旧密码相同");
        }
        // 设置新密码成功
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            // 更新用户信息
            tokenService.setLoginUser(loginUser);
            // 返回成功提示
            return AjaxResult.success();
        }
        // 返回错误提示
        return AjaxResult.error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar))
            {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}
