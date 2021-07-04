package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.vo.AdviceVo;
import com.ruoyi.project.storage.service.IAdviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 意见建议Controller
 *
 * @author yanjiaqi
 * @date 2020.04.29
 */
@RestController
@RequestMapping("/backend/advice")
@Api(tags = {"【后台端】5.3.8 意见建议管理：意见建议列表（分页）"})
public class BackendAdviceController extends BaseController {

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
    public BackendAdviceController(IAdviceService adviceService) {
        // 赋值
        this.adviceService = adviceService;
    }

    /**
     * 查询意见建议列表
     *
     * @param advice 广告
     * @return 分页结果
     */
    @Log(title = "5.3.8.1 意见建议列表（分页）", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    @ApiOperation(value = "5.3.8.1 意见建议列表（分页）", notes = "查询意见建议列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "int", value = "当前页数", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "int", value = "每页显示记录数", defaultValue = "10")
    })
    public TableDataInfo list(AdviceVo advice) {
        // 获取分页信息
        startPage();
        // 查询广告列表
        List<AdviceVo> list = adviceService.selectAdviceList(advice);
        // 返回响应请求分页数据
        return getDataTable(list);
    }

}
