package com.ruoyi.project.storage.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.PointRecord;
import com.ruoyi.project.storage.service.IPointRecordService;
import com.ruoyi.project.storage.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 积分记录Controller
 *
 * @author yanjiaqi
 * @date 2020.05.02
 */
@Slf4j
@RestController
@RequestMapping("/app/point")
@Api(tags = {"【手机端】5.2.7 积分记录管理：积分记录列表（分页），获取当前用户积分"})
public class AppPointRecordController extends BaseController {

    /**
     * 积分记录Service
     */
    private final IPointRecordService pointRecordService;

    /**
     * 用户信息Service
     */
    private final IUserService userService;

    /**
     * 构造方法注入
     *
     * @param pointRecordService 广告Service
     * @param userService        用户信息Service
     */
    @Autowired
    public AppPointRecordController(IPointRecordService pointRecordService, IUserService userService) {
        // 赋值
        this.pointRecordService = pointRecordService;
        // 赋值
        this.userService = userService;
    }

    /**
     * 查询积分记录列表
     *
     * @param pointRecord 广告
     * @return 分页结果
     */
    @Log(title = "5.2.7.1 积分记录列表（分页）", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    @ApiOperation(value = "5.2.7.1 积分记录列表（分页）", notes = "查询积分记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "int", value = "当前页数", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "int", value = "每页显示记录数", defaultValue = "10")
    })
    public TableDataInfo list(PointRecord pointRecord) {
        // 获取分页信息
        startPage();
        // 查询广告列表
        List<PointRecord> list = pointRecordService.selectRecordList(pointRecord.getUserId());
        // 返回响应请求分页数据
        return getDataTable(list);
    }

    /**
     * 获取当前用户积分
     *
     * @return 结果
     */
    @Log(title = "5.2.7.2 获取当前用户积分", businessType = BusinessType.OTHER)
    @GetMapping("/select")
    @ApiOperation(value = "5.2.7.2  获取当前用户积分", notes = " 获取当前用户积分")
    public AjaxResult select() {
        // 打印当前登录用户id
        log.info("当前用户id为{}", SecurityUtils.getUserId());
        // 返回成功结果
        AjaxResult ajax = AjaxResult.success();
        // 用户信息放入map
        ajax.put("data", userService.getCurrentPoints(SecurityUtils.getUserId()));
        // 返回响应结果
        return ajax;
    }

}
