package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.vo.BoxInfoVo;
import com.ruoyi.project.storage.service.IBoxInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 箱子信息Controller
 *
 * @author yanjiaqi
 * @date 2020.05.09
 */
@RestController
@RequestMapping("/backend/box/info")
@Api(tags = {"【后台端】5.3.5 箱子信息管理：箱子信息列表（分页）、箱子信息新增，箱子信息删除"})
public class BackendBoxInfoController extends BaseController {

    /**
     * 箱子信息Service
     */
    private final IBoxInfoService boxInfoService;

    /**
     * 构造方法注入
     *
     * @param boxInfoService 箱子信息Service
     */
    @Autowired
    public BackendBoxInfoController(IBoxInfoService boxInfoService) {
        // 赋值
        this.boxInfoService = boxInfoService;
    }

    /**
     * 查询箱子信息列表
     *
     * @param boxInfo 箱子信息
     * @return 分页结果
     */
    @Log(title = "5.3.5.1 箱子信息列表（分页）", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    @ApiOperation(value = "5.3.5.1 箱子信息列表（分页）", notes = "查询箱子信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "int", value = "当前页数", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "int", value = "每页显示记录数", defaultValue = "10")
    })
    public TableDataInfo list(BoxInfoVo boxInfo) {
        // 获取分页信息
        startPage();
        // 查询箱子信息列表
        List<BoxInfoVo> list = boxInfoService.selectBoxInfoVoList(boxInfo);
        // 返回响应请求分页数据
        return getDataTable(list);
    }

    /**
     * 新增箱子信息
     *
     * @param boxInfo 箱子信息
     * @return 响应结果
     */
    @Log(title = "5.3.5.2 箱子信息新增", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    @ApiOperation(value = "5.3.5.2 箱子信息新增", notes = "新增箱子信息")
    public AjaxResult add(@RequestBody BoxInfoVo boxInfo) {
        //返回响应结果
        return toAjax(boxInfoService.insertBoxInfo(boxInfo), "新增");
    }

    /**
     * 删除箱子信息
     *
     * @param ids 要删除的箱子id
     * @return 响应结果
     */
    @Log(title = "5.3.5.3 箱子信息删除", businessType = BusinessType.OTHER)
    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "5.3.5.3 箱子信息删除", notes = "删除箱子信息")
    public AjaxResult remove(@PathVariable Long[] ids) {
        //返回响应结果
        return toAjax(boxInfoService.deleteBoxInfoByIds(ids), "删除");
    }

}
