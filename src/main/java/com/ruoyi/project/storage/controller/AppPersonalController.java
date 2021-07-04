package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.enums.TerminalEnum;
import com.ruoyi.project.storage.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 手机端个人中心Controller
 *
 * @author yanjiaqi
 * @date 2020.05.03
 */
@RestController
@RequestMapping("/app/personal")
@Api(tags = {"【手机端】5.2.5 个人中心：修改密码"})
public class AppPersonalController extends BaseController {

    /**
     * 手机端个人中心Service
     */
    private final IUserService userService;

    /**
     * 构造方法注入
     *
     * @param userService 手机端个人中心Service
     */
    @Autowired
    public AppPersonalController(IUserService userService) {
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
    @Log(title = "5.2.5.1 修改密码", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePassword/{oldPassword}/{newPassword}")
    @ApiOperation(value = "5.2.5.1 修改密码", notes = "修改密码")
    public AjaxResult updatePassword(@PathVariable String oldPassword, @PathVariable String newPassword) {
        //返回响应结果
        return toAjax(userService.updateUserPassword(TerminalEnum.APP, oldPassword, newPassword));
    }

}
