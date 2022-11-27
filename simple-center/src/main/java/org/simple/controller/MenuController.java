package org.simple.controller;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.entity.Menu;
import org.simple.service.IMenuService;
import org.simple.utils.CommonResult;
import org.simple.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/menu")
@Tag(name = "menu", description = "菜单管理")
public class MenuController {
    private final IMenuService menuService;

    @GetMapping("treeAll")
    @Operation(summary = "查询菜单树")
    public List<Tree<String>> getTreeMenuAll() {
        return menuService.getTreeMenuAll();
    }

    @GetMapping("roleTreeAll")
    @Operation(summary = "查询权限菜单树")
    public List<Tree<String>> getRoleTreeAll() {
        return menuService.getRoleMenuAll();
    }

    @GetMapping("menuList")
    @Operation(summary = "根据条件查询菜单")
    public CommonResult getMenuList(Page page, Menu menu) {
        String name = menu.getName();
        menu.setName("");
        return CommonResult.success(menuService.page(page,Wrappers.query(menu).orderByAsc("sort")
                .like("name",name)));
    }

    @GetMapping("btnList")
    @Operation(summary = "查询菜单权限列表")
    public List<Menu> getBtnList(@RequestParam("id") String id) {
        Menu m = new Menu();
        m.setParentid(id);
        m.setType("b");
        return menuService.list(Wrappers.query(m));
    }

    @PostMapping("addMenu")
    @Operation(summary = "新增菜单信息")
    public Boolean addMenu(@RequestBody Menu menu) {
        //重新组装菜单信息表
        Menu menu1 = new Menu();
        menu1.setCreatetime(LocalDateTime.now());
        menu1.setUpdatetime(LocalDateTime.now());
        menu1.setId(RandomUtil.getMenuId());
        menu1.setType("c");
        menu1.setParentid(menu.getParentid());
        menu1.setComponent(menu.getComponent());
        menu1.setName(menu.getName());
        menu1.setMeta(menu.getMeta());
        menu1.setPath(menu.getPath());
        menu1.setSort(menu.getSort());
        menu1.setStatus("0");
        return menuService.save(menu1);
    }

    @PostMapping("editMenu")
    @Operation(summary = "修改菜单信息")
    public Boolean editMenu(@RequestBody Menu menu) {
        //重新组装菜单信息表
        Menu menu1 = new Menu();
        menu1.setUpdatetime(LocalDateTime.now());
        menu1.setId(menu.getId());
        menu1.setComponent(menu.getComponent());
        menu1.setName(menu.getName());
        menu1.setMeta(menu.getMeta());
        menu1.setPath(menu.getPath());
        menu1.setSort(menu.getSort());
        return menuService.updateById(menu1);
    }

    @DeleteMapping("delMenu")
    @Operation(summary = "删除菜单信息")
    public Boolean delMenu(@RequestParam("id") String id) {
        return menuService.delMenu(id);
    }

    @PostMapping("addBtnMenu")
    @Operation(summary = "新增菜单权限信息")
    public Boolean addBtnMenu(@RequestBody Menu menu) {
        Menu m = new Menu();
        m.setId(RandomUtil.getMenuBtnId());
        m.setStatus("0");
        m.setName(menu.getName());
        m.setType("b");
        m.setAuthcode(menu.getAuthcode());
        m.setParentid(menu.getParentid());
        m.setCreatetime(LocalDateTime.now());
        m.setUpdatetime(LocalDateTime.now());
        return menuService.save(m);

    }

    @PostMapping("editBtnMenu")
    @Operation(summary = "修改菜单权限信息")
    public Boolean editBtnMenu(@RequestBody Menu menu) {
        Menu m = new Menu();
        m.setId(menu.getId());
        m.setName(menu.getName());
        m.setAuthcode(menu.getAuthcode());
        m.setUpdatetime(LocalDateTime.now());
        return menuService.updateById(m);
    }

    @DeleteMapping("delBtnMenu")
    @Operation(summary = "删除菜单权限信息")
    public Boolean delBtnMenu(@RequestParam("id") String id) {
        return menuService.delBtnMenu(id);
    }
}
