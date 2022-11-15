package org.simple.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.entity.Role;
import org.simple.entity.RoleMenu;
import org.simple.service.RoleService;
import org.simple.utils.CommonResult;
import org.simple.utils.RedomUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 角色管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@AllArgsConstructor
@RequestMapping("/role")
@Tag(name = "role", description = "角色管理")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("list")
    @Operation(summary = "查询角色列表")
    public CommonResult list(Page page, Role role) {
        String r = StrUtil.isEmpty(role.getName()) ? "" : role.getName();
        role.setName(null);
        return CommonResult.success(roleService.page(page, Wrappers.query(role).like("name", r)));
    }

    @PostMapping("addRole")
    @Operation(summary = "新增角色信息")
    public CommonResult addRole(@RequestBody Role role) {
        //清洗重新定义对象
        Role r = new Role();
        r.setId(RedomUtil.getRoleId());
        r.setCreatetime(LocalDateTime.now());
        r.setRemark(role.getRemark());
        r.setName(role.getName());
        r.setStatus("0");
        r.setType("01");
        roleService.save(r);
        return CommonResult.successNodata("新增成功");
    }

    @PostMapping("editRole")
    @Operation(summary = "修改角色信息")
    public CommonResult editRole(@RequestBody Role role) {
        //清洗重新定义对象
        Role r = new Role();
        r.setId(role.getId());
        r.setRemark(role.getRemark());
        r.setName(role.getName());
        roleService.save(r);
        return CommonResult.successNodata("修改成功");
    }

    @DeleteMapping("delRole")
    @Operation(summary = "删除角色信息")
    public CommonResult delRole(@RequestParam("id") String id) {
        return roleService.delRole(id);
    }


    @GetMapping("roleMenu")
    @Operation(summary = "查询角色列表")
    public CommonResult roleMenu(@RequestParam("role") String role) {
        return roleService.queryRoleMenu(role);
    }

    @PostMapping("saveRoleMenu")
    @Operation(summary = "保存角色菜单权限信息")
    public CommonResult saveRoleMenu(@RequestBody RoleMenu roleMenu) {
        return roleService.insertRoleMenus(roleMenu);
    }

}
