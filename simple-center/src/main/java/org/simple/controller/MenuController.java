package org.simple.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.entity.Menu;
import org.simple.service.MenuService;
import org.simple.utils.CommonResult;
import org.simple.utils.RedomUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 菜单管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@AllArgsConstructor
@RequestMapping("/menu")
@Tag(name = "menu", description = "菜单管理")
public class MenuController {
    private final MenuService menuService;

    @GetMapping("treeAll")
    @Operation(summary = "查询菜单树")
    public CommonResult getTreeMenuAll() {
        return CommonResult.success(menuService.getTreeMenuAll());
    }

    @GetMapping("roleTreeAll")
    @Operation(summary = "查询权限菜单树")
    public CommonResult getRoleTreeAll() {
        return CommonResult.success(menuService.getRoleMenuAll());
    }

    @GetMapping("menuList")
    @Operation(summary = "根据条件查询菜单")
    public CommonResult getMenuList(Page page, Menu menu) {
        String name = menu.getName();
        menu.setName("");
        return CommonResult.success(menuService.page(page, Wrappers.query(menu).orderByAsc("sort")
                .like("name", name)));
    }

    @GetMapping("btnList")
    @Operation(summary = "查询菜单权限列表")
    public CommonResult getbtnList(@RequestParam("id") String id) {
        Menu m = new Menu();
        m.setParentid(id);
        m.setType("b");
        return CommonResult.success(menuService.list(Wrappers.query(m)));
    }

    @PostMapping("addMenu")
    @Operation(summary = "新增菜单信息")
    public CommonResult addMenu(@RequestBody Menu menu) {
        //重新组装菜单信息表
        Menu menu1 = new Menu();
        menu1.setCreatetime(LocalDateTime.now());
        menu1.setUpdatetime(LocalDateTime.now());
        menu1.setId(RedomUtil.getMenuId());
        menu1.setType("c");
        menu1.setParentid(menu.getParentid());
        menu1.setComponent(menu.getComponent());
        menu1.setName(menu.getName());
        menu1.setMeta(menu.getMeta());
        menu1.setPath(menu.getPath());
        menu1.setSort(menu.getSort());
        menu1.setStatus("0");
        menuService.save(menu1);
        return CommonResult.success("添加成功");
    }

    @PostMapping("editMenu")
    @Operation(summary = "修改菜单信息")
    public CommonResult editMenu(@RequestBody Menu menu) {
        //重新组装菜单信息表
        Menu menu1 = new Menu();
        menu1.setUpdatetime(LocalDateTime.now());
        menu1.setId(menu.getId());
        menu1.setComponent(menu.getComponent());
        menu1.setName(menu.getName());
        menu1.setMeta(menu.getMeta());
        menu1.setPath(menu.getPath());
        menu1.setSort(menu.getSort());
        menuService.updateById(menu1);
        return CommonResult.success("修改成功");
    }

    @DeleteMapping("delMenu")
    @Operation(summary = "删除菜单信息")
    public CommonResult delMenu(@RequestParam("id") String id) {
        return menuService.delMenu(id);
    }

    @PostMapping("addBtnMenu")
    @Operation(summary = "新增菜单权限信息")
    public CommonResult addBtnMenu(@RequestBody Menu menu) {
        Menu m = new Menu();
        m.setId(RedomUtil.getMenuBtnId());
        m.setStatus("0");
        m.setName(menu.getName());
        m.setType("b");
        m.setAuthcode(menu.getAuthcode());
        m.setParentid(menu.getParentid());
        m.setCreatetime(LocalDateTime.now());
        m.setUpdatetime(LocalDateTime.now());
        menuService.save(m);
        return CommonResult.successNodata("保存成功");
    }

    @PostMapping("editBtnMenu")
    @Operation(summary = "修改菜单权限信息")
    public CommonResult editBtnMenu(@RequestBody Menu menu) {
        Menu m = new Menu();
        m.setId(menu.getId());
        m.setName(menu.getName());
        m.setAuthcode(menu.getAuthcode());
        m.setUpdatetime(LocalDateTime.now());
        menuService.updateById(m);
        return CommonResult.successNodata("保存成功");
    }

    @DeleteMapping("delBtnMenu")
    @Operation(summary = "删除菜单权限信息")
    public CommonResult delBtnMenu(@RequestParam("id") String id) {
        return menuService.delBtnMenu(id);
    }
}
