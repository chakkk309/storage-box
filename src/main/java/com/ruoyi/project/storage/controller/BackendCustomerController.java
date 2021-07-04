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
 * 用户Controller
 *
 * @author yanjiaqi
 * @date 2020.05.05
 */
@RestController
@RequestMapping("/backend/customer")
@Api(tags = {"【后台端】5.3.3 客户管理：客户列表（分页）、客户新增、客户编辑、客户删除、客户重置密码，客户启用/停用"})
public class BackendCustomerController extends BaseController {

    /**
     * 客户Service
     */
    private final IUserService userService;

    /**
     * 构造方法注入
     *
     * @param userService 用户Service
     */
    public BackendCustomerController(IUserService userService) {
        // 赋值
        this.userService = userService;
    }

    /**
     * 查询客户列表
     *
     * @param user 客户
     * @return 分页结果
     */
    @Log(title = "5.3.3.1 客户列表（分页）", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    @ApiOperation(value = "5.3.3.1 客户列表（分页）", notes = "查询客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "int", value = "当前页数", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "int", value = "每页显示记录数", defaultValue = "10")
    })
    public TableDataInfo list(User user) {
        // 获取分页信息
        startPage();
        // 查询广告列表
        List<User> list = userService.selectUserList(TerminalEnum.APP, user);
        // 返回响应请求分页数据
        return getDataTable(list);
    }

    /**
     * 客户新增
     *
     * @param user 客户
     * @return 结果
     */
    @Log(title = "5.3.3.2客户新增", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    @ApiOperation(value = "5.3.3.2 客户新增", notes = "新增客户")
    public AjaxResult add(@RequestBody User user) {
        // 判断登录账号是否唯一
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(TerminalEnum.APP, user.getUserName()))) {
            // 返回新增失败错误信息（登录账号已存在）
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        // 判断手机号码是否唯一
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(TerminalEnum.APP, user))) {
            // 返回新增失败错误信息（手机号码已存在)
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        // 判断邮箱是否唯一
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(TerminalEnum.APP, user))) {
            // 返回新增失败错误信息(邮箱账号已存在）
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        } else {
            // 返回响应结果
            return toAjax(userService.insertUser(TerminalEnum.APP, user));
        }
    }

    /**
     * 客户编辑
     *
     * @param user 客户
     * @return 结果
     */
    @Log(title = "5.3.3.3 客户编辑", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    @ApiOperation(value = "5.3.3.3 客户编辑", notes = "编辑客户")
    public AjaxResult edit(@RequestBody User user) {
        // 返回响应结果
        return toAjax(userService.updateUser(TerminalEnum.APP, user), "修改");
    }

    /**
     * 批量删除客户
     *
     * @param userIds 删除的客户id
     * @return 返回结果
     */
    @Log(title = "5.3.3.4 客户删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{userIds}")
    @ApiOperation(value = "5.3.3.4 客户删除", notes = "删除客户")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        // 返回响应结果
        return toAjax(userService.deleteUserByIds(TerminalEnum.APP, userIds), "删除");
    }

    /**
     * 客户重置密码
     *
     * @param userIds 需要重置密码的客户id
     * @return 响应结果
     */
    @Log(title = "5.3.3.5 客户重置密码", businessType = BusinessType.UPDATE)
    @PutMapping("/reset/{userIds}")
    @ApiOperation(value = "5.3.3.5 客户重置密码", notes = "重置客户密码")
    public AjaxResult resetPwd(@PathVariable Long[] userIds) {
        // 返回响应结果
        return toAjax(userService.resetPwdUserByIds(TerminalEnum.APP, userIds), "重置密码");
    }

    /**
     * 启用/停用客户
     *
     * @param operate 操作（0：启用，1：停用）
     * @param userIds 需要操作的用户
     * @return 响应结果
     */
    @Log(title = "5.3.3.6 客户启用/停用", businessType = BusinessType.UPDATE)
    @PutMapping("/{operate}/{userIds}")
    @ApiOperation(value = "5.3.3.6 客户启用/停用", notes = "启用/停用客户")
    public AjaxResult enable(@PathVariable String operate, @PathVariable Long[] userIds) {
        // 返回响应结果
        return toAjax(userService.operateUserByIds(TerminalEnum.APP, operate, userIds));
    }

}
