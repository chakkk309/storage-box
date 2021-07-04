package com.ruoyi.project.storage.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.Address;
import com.ruoyi.project.storage.service.IAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 地址信息Controller
 *
 * @author yanjiaqi
 * @date 2020.05.09
 */
@RestController
@RequestMapping("/app/address")
@Api(tags = {"【手机端】5.2.6 地址管理：地址列表（分页）、地址新增、地址编辑、地址删除，设置默认地址"})
public class AppAddressController extends BaseController {

    /**
     * 地址Service
     */
    private final IAddressService addressService;

    /**
     * 构造方法注入
     *
     * @param addressService 地址信息
     */
    @Autowired
    public AppAddressController(IAddressService addressService) {
        // 赋值
        this.addressService = addressService;
    }

    /**
     * 查询地址列表
     *
     * @param address 地址信息
     * @return 返回响应请求分页数据
     */
    @Log(title = "5.2.6.1 地址列表（分页）", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    @ApiOperation(value = "5.2.6.1 地址列表（分页）", notes = "查询地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "int", value = "当前页数", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "int", value = "每页显示记录数", defaultValue = "10")
    })
    public TableDataInfo list(Address address) {
        // 获取分页信息
        startPage();
        // 查询地址列表
        List<Address> list = addressService.selectAddressList(address);
        // 返回响应请求分页数据
        return getDataTable(list);
    }

    /**
     * 新增地址列表
     *
     * @param address 地址
     * @return 响应结果
     */
    @Log(title = "5.2.6.2 地址新增", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    @ApiOperation(value = "5.2.6.2 地址新增", notes = "新增地址")
    public AjaxResult add(@RequestBody Address address) {
        // 返回响应结果
        return toAjax(addressService.insertAddress(address), "新增");
    }

    /**
     * 修改地址
     *
     * @param address 地址对象
     * @return 响应结果
     */
    @Log(title = "5.2.6.3 地址编辑", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    @ApiOperation(value = "5.2.6.3 地址编辑", notes = "修改地址")
    public AjaxResult edit(@RequestBody Address address) {
        // 返回响应结果
        return toAjax(addressService.updateAddress(address), "修改");
    }

    /**
     * 删除地址
     *
     * @param ids 需要删除的地址id
     * @return 响应结果
     */
    @Log(title = "5.2.6.4 地址删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "5.2.6.4 地址删除", notes = "删除地址")
    public AjaxResult remove(@PathVariable Long[] ids) {
        // 返回响应结果
        return toAjax(addressService.deleteAddressByIds(ids), "删除");
    }

    /**
     * 设置默认地址
     *
     * @param id 需要设置为默认地址的地址id
     * @return 响应结果
     */
    @Log(title = "5.2.6.5 设置默认地址", businessType = BusinessType.UPDATE)
    @PutMapping("/default/{id}")
    @ApiOperation(value = " 5.2.6.5 设置默认地址", notes = "设置默认地址")
    public AjaxResult setDefaultAddress(@PathVariable Long id) {
        // 返回响应结果
        return toAjax(addressService.setDefaultAddress(id, SecurityUtils.getUserId()), "设置默认");
    }

}
