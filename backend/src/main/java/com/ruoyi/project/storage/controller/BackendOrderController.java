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
 * 后台端订单Controller
 *
 * @author yanjiaqi
 * @date 2020.05.12
 */
@RestController
@RequestMapping("/backend/order")
@Api(tags = {"【后台端】5.3.6 订单管理：订单列表（分页）、订单列表、订单详情、订单操作，订单删除"})

public class BackendOrderController extends BaseController {

    /**
     * 订单Service
     */
    private final OrderServiceImpl orderService;

    /**
     * 构造方法注入
     *
     * @param orderService 订单
     */
    public BackendOrderController(OrderServiceImpl orderService) {
        // 赋值
        this.orderService = orderService;
    }

    /**
     * 查询订单列表
     *
     * @param orderVo 订单
     * @return 结果
     */
    @Log(title = "5.3.6.1 订单列表（分页）", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    @ApiOperation(value = "5.3.6.1 订单列表（分页）", notes = "查询订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "int", value = "当前页数", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "int", value = "每页显示记录数", defaultValue = "10")
    })
    public TableDataInfo list(OrderVo orderVo) {
        // 获取分页信息
        startPage();
        // 查询广告列表
        List<OrderVo> list = orderService.selectBackendOrderList(orderVo);
        // 返回响应请求分页数据
        return getDataTable(list);
    }

    /**
     * 订单详情
     *
     * @param id 订单id
     * @return 返回结果
     */
    @Log(title = "5.3.6.2 订单详情", businessType = BusinessType.OTHER)
    @GetMapping("/info/{id}")
    @ApiOperation(value = "5.3.6.2 订单详情", notes = "订单详情")
    public AjaxResult selectOrder(@PathVariable Long id) {
        // 返回查询结果
        return AjaxResult.success("操作成功", orderService.selectBackendOrder(id));
    }

    /**
     * 订单操作
     *
     * @param id      订单id
     * @param operate 订单操作（2：【发送空箱】；5：【收取重箱】；7：【发送重箱】；10：【收取空箱】）
     * @param version 版本号
     * @return 结果
     */
    @Log(title = "5.3.6.3 订单操作", businessType = BusinessType.UPDATE)
    @PutMapping("/operate/{id}/{operate}/{version}")
    @ApiOperation(value = "5.3.6.3 订单操作", notes = "操作订单")
    public AjaxResult operate(@PathVariable Long id, @PathVariable Integer operate, @PathVariable Long version,
                              @RequestBody OrderVo orderVo) {
        //返回响应结果
        return toAjax(orderService.operateOrderByIds(id, operate, version, orderVo));
    }

    /**
     * 订单删除
     *
     * @param ids 要删除的订单id
     * @return 结果
     */
    @Log(title = "5.3.6.4 订单删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "5.3.6.4 订单删除", notes = "删除订单")
    public AjaxResult remove(@PathVariable Long[] ids) {
        // 返回响应结果
        return toAjax(orderService.deleteOrderByIds(ids), "删除");
    }

}
