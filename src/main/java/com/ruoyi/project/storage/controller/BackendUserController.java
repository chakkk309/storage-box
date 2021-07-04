package com.ruoyi.project.storage.controller;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.common.enums.TerminalEnum;
import com.ruoyi.project.storage.domain.User;
import com.ruoyi.project.storage.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户Controller
 *
 * @author yanjiaqi
 * @date 2020.05.01
 */
@RestController
@RequestMapping("/backend/user")
@Api(tags = {"【后台端】5.3.2 用户管理：用户列表（分页），用户新增、用户编辑、用户删除，用户重置密码、用户启用/停用"})
public class BackendUserController extends BaseController {

    /**
     * 客户Service
     */
    private final IUserService userService;

    /**
     * 构造方法注入
     *
     * @param userService 用户Service
     */
    public BackendUserController(IUserService userService) {
        // 赋值
        this.userService = userService;
    }

    /**
     * 查询用户列表
     *
     * @param user 用户
     * @return 分页结果
     */
    @Log(title = "5.3.2.1 用户列表（分页）", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    @ApiOperation(value = "5.3.2.1 用户列表（分页）", notes = "查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "int", value = "当前页数", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "int", value = "每页显示记录数", defaultValue = "10")
    })
    public TableDataInfo list(User user) {
        // 获取分页信息
        startPage();
        // 查询广告列表
        List<User> list = userService.selectUserList(TerminalEnum.BACKEND, user);
        // 返回响应请求分页数据
        return getDataTable(list);
    }

    /**
     * 用户新增
     *
     * @param user 用户
     * @return 结果
     */
    @Log(title = "5.3.2.2 用户新增", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    @ApiOperation(value = "5.3.2.2 用户新增", notes = "新增用户")
    public AjaxResult add(@RequestBody User user) {
        // 判断登录账号是否唯一
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(TerminalEnum.BACKEND, user.getUserName()))) {
            // 返回新增失败错误信息（登录账号已存在）
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        // 判断手机号码是否唯一
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(TerminalEnum.BACKEND, user))) {
            // 返回新增失败错误信息（手机号码已存在)
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        // 判断邮箱是否唯一
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(TerminalEnum.BACKEND, user))) {
            // 返回新增失败错误信息(邮箱账号已存在）
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        } else {
            // 返回响应结果
            return toAjax(userService.insertUser(TerminalEnum.BACKEND, user), "新增");
        }
    }

    /**
     * 用户编辑
     *
     * @param user 用户
     * @return 结果
     */
    @Log(title = "5.3.2.3 用户编辑", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    @ApiOperation(value = "5.3.2.3 用户编辑", notes = "编辑用户")
    public AjaxResult edit(@RequestBody User user) {
        // 返回结果
        return toAjax(userService.updateUser(TerminalEnum.BACKEND, user));
    }

    /**
     * 批量删除用户
     *
     * @param userIds 删除的用户id
     * @return 返回结果
     */
    @Log(title = "5.3.2.4 用户删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{userIds}")
    @ApiOperation(value = "5.3.2.4 用户删除", notes = "删除用户")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        // 返回响应结果
        return toAjax(userService.deleteUserByIds(TerminalEnum.BACKEND, userIds), "删除");
    }

    /**
     * 客户重置密码
     *
     * @param userIds 需要重置密码的客户id
     * @return 响应结果
     */
    @Log(title = "5.3.2.5 用户重置密码", businessType = BusinessType.UPDATE)
    @PutMapping("/reset/{userIds}")
    @ApiOperation(value = "5.3.2.5 用户重置密码", notes = "重置用户密码")
    public AjaxResult resetPwd(@PathVariable Long[] userIds) {
        // 返回响应结果
        return toAjax(userService.resetPwdUserByIds(TerminalEnum.BACKEND, userIds), "重置密码");
    }

    /**
     * 启用/停用客户
     *
     * @param operate 操作（0：启用，1：停用）
     * @param userIds 需要操作的用户id
     * @return 响应结果
     */
    @Log(title = "5.3.2.6 用户启用/停用", businessType = BusinessType.UPDATE)
    @PutMapping("/{operate}/{userIds}")
    @ApiOperation(value = "5.3.2.6 用户启用/停用", notes = "启用/停用用户")
    public AjaxResult enable(@PathVariable String operate, @PathVariable Long[] userIds) {
        //返回响应结果
        return toAjax(userService.operateUserByIds(TerminalEnum.BACKEND, operate, userIds));
    }

}
