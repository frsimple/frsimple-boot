package org.simple.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.simple.entity.Tenant;
import org.simple.service.TenantService;
import org.simple.utils.CommonResult;
import org.simple.utils.RedomUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 租户管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@AllArgsConstructor
@RequestMapping("/tenant")
public class TenantController {
    private final TenantService tenantService;

    @GetMapping("list")
    @Operation(summary = "查询机构信息列表")
    public CommonResult list(Page page, Tenant tenant) {
        String name = "";
        if (StrUtil.isNotEmpty(tenant.getName())) {
            name = tenant.getName();
        }
        tenant.setName(null);
        return CommonResult.success(tenantService.page(page, Wrappers.query(tenant).like("name", name)));
    }

    @GetMapping("getTenant")
    @Operation(summary = "根据名称查询机构")
    public CommonResult getOne(@RequestParam("name") String name) {
        Tenant tenant = new Tenant();
        tenant.setName(name);
        return CommonResult.success(tenantService.list(Wrappers.query(tenant)));
    }

    @PostMapping("addTenant")
    @Operation(summary = "新增机构信息")
    public CommonResult addTenant(@RequestBody Tenant tenant) {
        tenant.setId(RedomUtil.getTenantId());
        tenant.setCreatedate(LocalDateTime.now());
        tenant.setUpdatedate(LocalDateTime.now());
        tenantService.save(tenant);
        return CommonResult.successNodata("新增成功");
    }

    @PostMapping("editTenant")
    @Operation(summary = "修改机构信息")
    public CommonResult editTenant(@RequestBody Tenant tenant) {
        tenant.setUpdatedate(LocalDateTime.now());
        tenantService.updateById(tenant);
        return CommonResult.successNodata("修改成功");
    }

    @DeleteMapping("delTenant/{id}")
    @Operation(summary = "删除机构信息")
    public CommonResult delTenant(@PathVariable("id") String id) {
        //判断机构下面是否关联的有用户
        Integer count = tenantService.selectCount(id);
        if (count != 0) {
            return CommonResult.failed("存在关联的用户，请先取消关联");
        }
        tenantService.removeById(id);
        return CommonResult.successNodata("删除成功");
    }

}
