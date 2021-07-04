package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.enums.TerminalEnum;
import com.ruoyi.project.common.util.CheckUtil;
import com.ruoyi.project.storage.domain.User;
import com.ruoyi.project.storage.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 手机端登录Controller
 *
 * @author yanjiaqi
 * @date 2020.05.04
 */
@RestController
@RequestMapping("/app")
@Api(tags = {"【手机端】5.2.1 注册：注册"})
public class AppRegistController extends BaseController {

    /**
     * 客户Service
     */
    private final IUserService userService;

    /**
     * 构造方法注入
     *
     * @param userService 用户Service
     */
    public AppRegistController(IUserService userService) {
        // 赋值
        this.userService = userService;
    }

    /**
     * 手机端用户注册
     *
     * @param user 客户
     * @return 响应结果
     */
    @Log(title = "5.2.1.1 手机端注册", businessType = BusinessType.INSERT)
    @PostMapping("/regist")
    @ApiOperation(value = "5.2.1.1 手机端注册", notes = "手机端注册")
    public AjaxResult add(@RequestBody User user) {
        // 校验用户是否唯一
        AjaxResult ajaxResult = CheckUtil.checkUserUnique(user, 1);
        // 返回通用返回实体（非分页）
        return ajaxResult == null ? toAjax(userService.insertUser(TerminalEnum.REGIST, user)) : ajaxResult;
    }

}
