package org.simple.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.entity.Role;
import org.simple.entity.RoleMenu;
import org.simple.service.IRoleService;
import org.simple.utils.CommonResult;
import org.simple.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/role")
@Tag(name = "role", description = "角色管理")
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("list")
    @Operation(summary = "查询角色列表")
    public IPage<List<Role>> list(Page page, Role role) {
        String r = StrUtil.isEmpty(role.getName()) ? "" : role.getName();
        role.setName(null);
        return roleService.page(page, Wrappers.query(role).like("name", r));
    }

    @PostMapping("addRole")
    @Operation(summary = "新增角色信息")
    public Boolean addRole(@RequestBody Role role) {
        //清洗重新定义对象
        Role r = new Role();
        r.setId(RandomUtil.getRoleId());
        r.setCreatetime(LocalDateTime.now());
        r.setRemark(role.getRemark());
        r.setName(role.getName());
        r.setStatus("0");
        r.setType("01");
        return roleService.save(r);
    }

    @PostMapping("editRole")
    @Operation(summary = "修改角色信息")
    public Boolean editRole(@RequestBody Role role) {
        //清洗重新定义对象
        Role r = new Role();
        r.setId(role.getId());
        r.setRemark(role.getRemark());
        r.setName(role.getName());
        return roleService.save(r);
    }

    @DeleteMapping("delRole")
    @Operation(summary = "删除角色信息")
    public CommonResult delRole(@RequestParam("id") String id) {
        return roleService.delRole(id);
    }


    @GetMapping("roleMenu")
    @Operation(summary = "查询角色菜单列表")
    public CommonResult roleMenu(@RequestParam("role") String role) {
        return roleService.queryRoleMenu(role);
    }

    @PostMapping("saveRoleMenu")
    @Operation(summary = "保存角色菜单权限信息")
    public CommonResult saveRoleMenu(@RequestBody RoleMenu roleMenu) {
        return roleService.insertRoleMenus(roleMenu);
    }

}
