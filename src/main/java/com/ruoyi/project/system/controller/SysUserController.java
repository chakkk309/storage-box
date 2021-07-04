package com.ruoyi.project.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysPostService;
import com.ruoyi.project.system.service.ISysRoleService;
import com.ruoyi.project.system.service.ISysUserService;

/**
 * 用户信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        // 获取分页信息
        startPage();
        // 查询用户列表
        List<SysUser> list = userService.selectUserList(user);
        // 返回通用实体（分页）
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public AjaxResult export(SysUser user)
    {
        // 查询用户列表
        List<SysUser> list = userService.selectUserList(user);
        // Excel处理
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        // 导出Excel形式的用户数据
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        // Excel处理
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        // 将Excel形式的用户数据导入
        List<SysUser> userList = util.importExcel(file.getInputStream());
        // 获取当前登录用户信息
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 获得操作人
        String operName = loginUser.getUsername();
        // 获得导入的用户数据
        String message = userService.importUser(userList, updateSupport, operName);
        // 返回成功的消息（msg）
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        // 返回操作成功实体
        AjaxResult ajax = AjaxResult.success();
        // 把所有角色（映射）放入该实体
        ajax.put("roles", roleService.selectRoleAll());
        // 把所有岗位（映射）放入该实体
        ajax.put("posts", postService.selectPostAll());
        // 用户id不为空
        if (StringUtils.isNotNull(userId))
        {
            // 将数据对象放入实体（用户id）
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            // 把用户角色（映射）放入该实体
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            // 把用户岗位（映射）放入该实体
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        // 返回实体
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user)
    {
        // 用户账号已存在
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName())))
        {
            // 返回错误提示
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        // 手机号码已存在
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            // 返回错误提示
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        // 邮箱已存在
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            // 返回错误提示
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        // 设置创建人
        user.setCreateBy(SecurityUtils.getUsername());
        // 设置用户密码
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        // 返回响应结果
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        // 判断用户权限（是否允许操作）
        userService.checkUserAllowed(user);
        // 手机号已存在（除当前数据外）
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            // 返回错误提示
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        // 邮箱已存在
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            // 返回错误提示
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        // 设置更新人
        user.setUpdateBy(SecurityUtils.getUsername());
        // 返回响应结果
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        // 返回响应实体
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user)
    {
        // 判断用户权限（是否允许操作）
        userService.checkUserAllowed(user);
        // 设置密码
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        // 设置更新人
        user.setUpdateBy(SecurityUtils.getUsername());
        // 返回响应实体
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        // 判断用户权限（是否允许操作）
        userService.checkUserAllowed(user);
        // 设置更新人
        user.setUpdateBy(SecurityUtils.getUsername());
        // 返回响应实体
        return toAjax(userService.updateUserStatus(user));
    }
}