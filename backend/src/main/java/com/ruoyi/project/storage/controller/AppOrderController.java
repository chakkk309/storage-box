package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.vo.OrderVo;
import com.ruoyi.project.storage.service.impl.OrderServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 手机端订单Controller
 *
 * @author yanjiaqi
 * @date 2020.05.14
 */
@RestController
@RequestMapping("/app/order")
@Api(tags = {"【手机端】5.2.4 订单管理：订单列表（分页）、订单详情，订单操作"})

public class AppOrderController extends BaseController {

    /**
     * 订单Service
     */
    private final OrderServiceImpl orderService;

    /**
     * 构造方法注入
     *
     * @param orderService 订单
     */
    public AppOrderController(OrderServiceImpl orderService) {
        // 赋值
        this.orderService = orderService;
    }

    /**
     * 查询订单列表
     *
     * @param orderVo 订单
     * @return 结果
     */
    @Log(title = "5.2.4.1 订单列表（分页）", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    @ApiOperation(value = "5.2.4.1 订单列表（分页）", notes = "查询订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "int", value = "当前页数", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "int", value = "每页显示记录数", defaultValue = "10")
    })
    public TableDataInfo list(OrderVo orderVo) {
        // 获取分页信息
        startPage();
        // 查询广告列表
        List<OrderVo> list = orderService.selectAppOrderList(orderVo);
        // 返回响应请求分页数据
        return getDataTable(list);
    }

    /**
     * 订单详情
     *
     * @param id 订单id
     * @return 返回结果
     */
    @Log(title = "5.2.4.2 订单详情", businessType = BusinessType.OTHER)
    @GetMapping("/info/{id}")
    @ApiOperation(value = "5.2.4.2 订单详情", notes = "订单详情")
    public AjaxResult selectAppOrder(@PathVariable Long id) {
        // 返回查询结果
        return AjaxResult.success(orderService.selectAppOrder(id));
    }

    /**
     * 订单操作
     *
     * @param id      订单id
     * @param operate 订单操作（3：【收取空箱】；4：【发送重箱】；6：【预约提取】；8：【收取重箱】；9：【发送空箱】；10：【删除订单】）
     * @param version 版本号
     * @return 结果
     */
    @Log(title = "5.2.4.3 订单操作", businessType = BusinessType.UPDATE)
    @PutMapping("/operate/{id}/{operate}/{version}")
    @ApiOperation(value = "5.2.4.3 订单操作", notes = "操作订单")
    public AjaxResult operate(@PathVariable Long id, @PathVariable Integer operate, @PathVariable Long version, @RequestBody OrderVo orderVo) {
        //返回响应结果
        return toAjax(orderService.operateOrderByIds(id, operate, version, orderVo));
    }

}
