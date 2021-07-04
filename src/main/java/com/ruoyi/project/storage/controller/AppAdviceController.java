package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.vo.AdviceVo;
import com.ruoyi.project.storage.service.IAdviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 意见建议Controller
 *
 * @author yanjiaqi
 * @date 2020.04.29
 */
@RestController
@RequestMapping("/app/personal")
@Api(tags = {"【手机端】5.2.8 意见建议管理：意见建议新增"})
public class AppAdviceController extends BaseController {

    /**
     * 意见建议Service
     */
    private final IAdviceService adviceService;

    /**
     * 构造方法注入
     *
     * @param adviceService 广告Service
     */
    @Autowired
    public AppAdviceController(IAdviceService adviceService) {
        // 赋值
        this.adviceService = adviceService;
    }

    /**
     * 新增意见建议
     *
     * @param advice 意见建议对象
     * @return 响应结果
     */
    @Log(title = "5.2.8.1 意见建议新增", businessType = BusinessType.INSERT)
    @PostMapping("/advice")
    @ApiOperation(value = "5.2.8.1 意见建议新增", notes = "新增意见建议")
    public AjaxResult add(@RequestBody AdviceVo advice) {
        // 返回响应结果
        return toAjax(adviceService.insertAdvice(advice), "新增");
    }

}
