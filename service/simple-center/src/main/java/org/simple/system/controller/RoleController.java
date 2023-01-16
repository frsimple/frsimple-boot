package org.simple.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.github.yitter.idgen.YitIdHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.dto.IdsModel;
import org.simple.dto.PageResult;
import org.simple.system.dto.role.RoleQuery;
import org.simple.system.entity.RoleEntity;
import org.simple.system.entity.RoleMenuEntity;
import org.simple.exception.CustomException;
import org.simple.system.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/system/role")
@Tag(name = "role", description = "角色管理")
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("list")
    @Operation(summary = "查询角色列表")
    @SaCheckPermission(value = {"system:role:query"}, mode = SaMode.OR)
    public PageResult<RoleEntity> list(RoleQuery query) {
        List<RoleEntity> list = roleService.list(query);
        PageResult<RoleEntity> pageResult = new PageResult<>(list);
        pageResult.setTotal(query.getTotal());
        return pageResult;
    }

    @GetMapping("allList")
    @Operation(summary = "查询角色列表")
    @SaCheckPermission(value = {"system:role:query"}, mode = SaMode.OR)
    public List<RoleEntity> allList() {
        return roleService.list();
    }

    @PostMapping("addRole")
    @Operation(summary = "新增角色信息")
    @SaCheckPermission(value = {"system:role:add"}, mode = SaMode.OR)
    public Boolean addRole(@RequestBody RoleEntity roleEntity) {
        roleEntity.setId(String.valueOf(YitIdHelper.nextId()));
        roleEntity.setStatus("0");
        roleEntity.setType("01");
        return roleService.save(roleEntity);
    }

    @PostMapping("editRole")
    @Operation(summary = "修改角色信息")
    @SaCheckPermission(value = {"system:role:edit"}, mode = SaMode.OR)
    public Boolean editRole(@RequestBody RoleEntity entity) {
        return roleService.updateById(entity);
    }

    @PostMapping("delRole")
    @Operation(summary = "删除角色信息")
    @SaCheckPermission(value = {"system:role:del"}, mode = SaMode.OR)
    public Boolean delRole(@RequestBody IdsModel model) throws CustomException {
        return roleService.delRole(model.getId());
    }

    @GetMapping("roleMenu")
    @Operation(summary = "查询角色菜单列表")
    public List<String> roleMenu(@RequestParam("role") String role) {
        return roleService.queryRoleMenu(role);
    }

    @PostMapping("saveRoleMenu")
    @Operation(summary = "保存角色菜单权限信息")
    @SaCheckPermission(value = {"system:role:edit"}, mode = SaMode.OR)
    public Boolean saveRoleMenu(@RequestBody RoleMenuEntity roleMenuEntity) {
        return roleService.insertRoleMenus(roleMenuEntity);
    }
}
