package org.simple.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.entity.Tenant;
import org.simple.enums.system.ResultCode;
import org.simple.exception.CustomException;
import org.simple.service.ITenantService;
import org.simple.utils.RandomUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 租户管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/tenant")
@Tag(name = "tenant", description = "租户管理")
public class TenantController {
    private final ITenantService tenantService;

    @GetMapping("list")
    @Operation(summary = "查询机构信息列表")
    @SaCheckPermission(value = {"system:tenant:query"}, mode = SaMode.OR)
    public IPage<Tenant> list(Page page, Tenant tenant) {
        String name = "";
        if (StrUtil.isNotEmpty(tenant.getName())) {
            name = tenant.getName();
        }
        tenant.setName(null);
        return tenantService.page(page, Wrappers.query(tenant).like("name", name));
    }

    @GetMapping("getTenant")
    @Operation(summary = "根据名称查询机构")
    @SaCheckPermission(value = {"system:tenant:query"}, mode = SaMode.OR)
    public List<Tenant> getOne(@RequestParam("name") String name) {
        Tenant tenant = new Tenant();
        tenant.setName(name);
        return tenantService.list(Wrappers.query(tenant));
    }

    @PostMapping("addTenant")
    @Operation(summary = "新增机构信息")
    @SaCheckPermission(value = {"system:tenant:add"}, mode = SaMode.OR)
    public Boolean addTenant(@RequestBody Tenant tenant) {
        tenant.setId(RandomUtil.getTenantId());
        tenant.setCreatedate(LocalDateTime.now());
        tenant.setUpdatedate(LocalDateTime.now());
        return tenantService.save(tenant);
    }

    @PostMapping("editTenant")
    @Operation(summary = "修改机构信息")
    @SaCheckPermission(value = {"system:tenant:edit"}, mode = SaMode.OR)
    public Boolean editTenant(@RequestBody Tenant tenant) {
        tenant.setUpdatedate(LocalDateTime.now());
        return tenantService.updateById(tenant);
    }

    @DeleteMapping("delTenant/{id}")
    @Operation(summary = "删除机构信息")
    @SaCheckPermission(value = {"system:tenant:del"}, mode = SaMode.OR)
    public Boolean delTenant(@PathVariable("id") String id) throws CustomException {
        //判断机构下面是否关联的有用户
        Integer count = tenantService.selectCount(id);
        if (count != 0) {
            throw new CustomException(ResultCode.DB003.getCode());
        }
        return tenantService.removeById(id);
    }
}
