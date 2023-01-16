package org.simple.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yitter.idgen.YitIdHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.dto.PageModel;
import org.simple.dto.PageResult;
import org.simple.system.entity.TenantEntity;
import org.simple.enums.system.ResultCodeEnum;
import org.simple.exception.CustomException;
import org.simple.system.service.ITenantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户管理
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/system/tenant")
@Tag(name = "tenant", description = "租户管理")
public class TenantController {
    private final ITenantService tenantService;

    @GetMapping("list")
    @Operation(summary = "查询机构信息列表分页")
    @SaCheckPermission(value = {"system:tenant:query"}, mode = SaMode.OR)
    public PageResult<TenantEntity> list(PageModel pageModel) {
        List<TenantEntity> list = tenantService.list(pageModel);
        PageResult<TenantEntity> pageResult = new PageResult<>(list);
        pageResult.setTotal(pageModel.getTotal());
        return pageResult;
    }

    @GetMapping("allList")
    @Operation(summary = "查询机构信息列表全部")
    @SaCheckPermission(value = {"system:tenant:query"}, mode = SaMode.OR)
    public List<TenantEntity> allList(PageModel pageModel) {
        return tenantService.allList(pageModel);
    }

    @GetMapping("getTenant")
    @Operation(summary = "根据名称查询机构")
    @SaCheckPermission(value = {"system:tenant:query"}, mode = SaMode.OR)
    public List<TenantEntity> getOne(@RequestParam("name") String name) {
        TenantEntity tenantEntity = new TenantEntity();
        tenantEntity.setName(name);
        return tenantService.list(Wrappers.query(tenantEntity));
    }

    @PostMapping("addTenant")
    @Operation(summary = "新增机构信息")
    @SaCheckPermission(value = {"system:tenant:add"}, mode = SaMode.OR)
    public Boolean addTenant(@RequestBody TenantEntity tenantEntity) {
        tenantEntity.setId(String.valueOf(YitIdHelper.nextId()));
        return tenantService.save(tenantEntity);
    }

    @PostMapping("editTenant")
    @Operation(summary = "修改机构信息")
    @SaCheckPermission(value = {"system:tenant:edit"}, mode = SaMode.OR)
    public Boolean editTenant(@RequestBody TenantEntity tenantEntity) {
        return tenantService.updateById(tenantEntity);
    }

    @PostMapping("delTenant/{id}")
    @Operation(summary = "删除机构信息")
    @SaCheckPermission(value = {"system:tenant:del"}, mode = SaMode.OR)
    public Boolean delTenant(@PathVariable("id") String id) throws CustomException {
        //判断机构下面是否关联的有用户
        Integer count = tenantService.selectCount(id);
        if (count != 0) {
            throw new CustomException(ResultCodeEnum.DB5003.getCode());
        }
        return tenantService.removeById(id);
    }
}
