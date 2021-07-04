package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.Advertisement;
import com.ruoyi.project.storage.service.IAdvertisementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台端广告Controller
 *
 * @author yanjiaqi
 * @date 2020.04.28
 */
@RestController
@RequestMapping("/backend/advertisement")
@Api(tags = {"【后台端】5.3.7 广告管理：广告列表（分页）、广告新增、广告编辑、广告删除，广告启用/停用"})
public class BackendAdvertisementController extends BaseController {

    /**d
     * 广告Service
     */
    private final IAdvertisementService advertisementService;

    /**
     * 构造方法注入
     *
     * @param advertisementService 广告Service
     */
    @Autowired
    public BackendAdvertisementController(IAdvertisementService advertisementService) {
        // 赋值
        this.advertisementService = advertisementService;
    }

    /**
     * 查询广告列表
     *
     * @param advertisement 广告
     * @return 分页结果
     */
    @Log(title = "5.3.7.1 广告列表（分页）", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    @ApiOperation(value = "5.3.7.1 广告列表（分页）", notes = "查询广告列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "int", value = "当前页数", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "int", value = "每页显示记录数", defaultValue = "10")
    })
    public TableDataInfo list(Advertisement advertisement) {
        // 获取分页信息
        startPage();
        // 查询广告列表
        List<Advertisement> list = advertisementService.selectAdvertisementList(advertisement);
        // 返回响应请求分页数据
        return getDataTable(list);
    }

    /**
     * 新增广告
     *
     * @param advertisement 广告对象
     * @return 响应结果
     */
    @Log(title = "5.3.7.2 广告新增", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    @ApiOperation(value = "5.3.7.2 广告新增", notes = "新增广告")
    public AjaxResult add(@RequestBody Advertisement advertisement) {
        // 返回响应结果
        return toAjax(advertisementService.insertAdvertisement(advertisement), " 新增");
    }

    /**
     * 修改广告
     *
     * @param advertisement 广告对象
     * @return 响应结果
     */
    @Log(title = "5.3.7.3 广告编辑", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    @ApiOperation(value = "5.3.7.3 广告编辑", notes = "修改广告")
    public AjaxResult edit(@RequestBody Advertisement advertisement) {
        //返回响应结果
        return toAjax(advertisementService.updateAdvertisement(advertisement), "修改");
    }

    /**
     * 删除广告
     *
     * @param ids 需要删除的广告id
     * @return 响应结果
     */
    @Log(title = "5.3.7.4 广告删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "5.3.7.4 广告删除", notes = "删除广告")
    public AjaxResult remove(@PathVariable Long[] ids) {
        //返回响应结果
        return toAjax(advertisementService.deleteAdvertisementByIds(ids), "删除");
    }

    /**
     * 启用/停用广告
     *
     * @param operate 操作类型（0：启用，1：停用）
     * @param ids     需要进行操作的广告id
     * @return 响应结果
     */
    @Log(title = "5.3.7.5 广告启用/停用", businessType = BusinessType.UPDATE)
    @PutMapping("/{operate}/{ids}")
    @ApiOperation(value = "5.3.7.5 广告启用/停用", notes = "启用/停用广告")
    public AjaxResult enable(@PathVariable String operate, @PathVariable Long[] ids) {
        //返回响应结果
        return toAjax(advertisementService.operateAdvertisementByIds(operate, ids));
    }

}
