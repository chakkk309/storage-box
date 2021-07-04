package com.ruoyi.project.storage.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.vo.OrderVo;
import com.ruoyi.project.storage.service.impl.AddressServiceImpl;
import com.ruoyi.project.storage.service.impl.BoxStandardServiceImpl;
import com.ruoyi.project.storage.service.impl.OrderServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 空箱预约Controller
 *
 * @author yanjiaqi
 * @date 2020.05.11
 */
@RestController
@RequestMapping("/app/box")
@Api(tags = {"【手机端】5.2.3 空箱预约：获取当前用户默认收货地址、有效箱子规格选择（非分页），预约"})

public class AppBoxController extends BaseController {

    /**
     * 地址Service
     */
    private final AddressServiceImpl addressService;

    /**
     * 箱子规格Service
     */
    private final BoxStandardServiceImpl boxStandardService;

    /**
     * 订单Service
     */
    private final OrderServiceImpl orderService;

    /**
     * 构造方法注入
     *
     * @param addressService     地址Service
     * @param boxStandardService 箱子规格Service
     * @param orderService       订单Service
     */
    public AppBoxController(AddressServiceImpl addressService, BoxStandardServiceImpl boxStandardService, OrderServiceImpl orderService) {
        // 赋值
        this.addressService = addressService;
        // 赋值
        this.boxStandardService = boxStandardService;
        // 赋值
        this.orderService = orderService;
    }

    /**
     * 获取当前用户默认收货地址
     *
     * @return 结果
     */
    @Log(title = "5.2.3.1 获取当前用户默认收货地址", businessType = BusinessType.OTHER)
    @GetMapping("/defaultAddress")
    @ApiOperation(value = "5.2.3.1 获取当前用户默认收货地址", notes = "获取当前用户默认收货地址")
    public AjaxResult getDefaultAddress() {
        // 返回成功结果
        AjaxResult ajax = AjaxResult.success();
        // 用户信息放入map
        ajax.put("data", addressService.getDefaultAddress(SecurityUtils.getUserId()));
        // 返回响应结果
        return ajax;
    }

    /**
     * 有效箱子规格选择
     *
     * @return 结果
     */
    @Log(title = "5.2.3.2 有效箱子规格选择（非分页）", businessType = BusinessType.OTHER)
    @GetMapping("/standard/select")
    @ApiOperation(value = "5.2.3.2 有效箱子规格选择（非分页)", notes = "有效箱子规格选择（非分页)")
    public AjaxResult getStandSelect() {
        // 返回响应结果
        return AjaxResult.success(boxStandardService.selectBoxStandard());
    }

    /**
     * 预约
     *
     * @return 结果
     */
    @Log(title = "5.2.3.3 预约", businessType = BusinessType.INSERT)
    @PostMapping("/order")
    @ApiOperation(value = "5.2.3.3 预约", notes = "预约")
    public AjaxResult order(@RequestBody OrderVo orderVo) {
        // 返回响应结果
        return toAjax(orderService.autoAssign(orderVo), "预约");
    }

}
