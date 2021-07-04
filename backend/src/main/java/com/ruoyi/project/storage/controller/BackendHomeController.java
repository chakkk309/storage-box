package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.enums.TerminalEnum;
import com.ruoyi.project.storage.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台端修改密码Controller
 *
 * @author yanjiaqi
 * @date 2020.05.03
 */
@RestController
@RequestMapping("/backend/home/updatePassword")
@Api(tags = {"【后台端】5.3.1 首页：修改密码"})
public class BackendHomeController extends BaseController {

    /**
     * 客户Service
     */
    private final IUserService userService;

    /**
     * 构造方法注入
     *
     * @param userService 用户Service
     */
    public BackendHomeController(IUserService userService) {
        // 赋值
        this.userService = userService;
    }

    /**
     * 修改密码
     *
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     * @return 响应结果
     */
    @Log(title = "后台端用户修改密码", businessType = BusinessType.UPDATE)
    @PutMapping("/{oldPassword}/{newPassword}")
    @ApiOperation(value = "5.3.1.1 修改密码", notes = "修改密码")
    public AjaxResult updateUserPwd(@PathVariable String newPassword, @PathVariable String oldPassword) {
        // 获取当前用户身份信息
        return toAjax(userService.updateUserPassword(TerminalEnum.BACKEND, oldPassword, newPassword));
    }

}
