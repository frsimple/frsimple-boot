package org.simple.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yitter.idgen.YitIdHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.dto.IdsModel;
import org.simple.dto.PageResult;
import org.simple.system.dto.menu.MenuQuery;
import org.simple.system.dto.menu.MenuTreeDto;
import org.simple.system.entity.MenuEntity;
import org.simple.system.service.IMenuService;
import org.simple.utils.CommonResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/system/menu")
@Tag(name = "menu", description = "菜单管理")
public class MenuController {
    private final IMenuService menuService;

    @GetMapping("treeAll")
    @Operation(summary = "查询菜单树")
    @SaCheckPermission(value = {"center:menu:query"}, mode = SaMode.OR)
    public List<MenuTreeDto> getTreeMenuAll() {
        return menuService.getTreeMenuAll();
    }

    @GetMapping("roleTreeAll")
    @Operation(summary = "查询权限菜单树")
    public List<MenuTreeDto> getRoleTreeAll() {
        return menuService.getRoleMenuAll();
    }

    @GetMapping("menuList")
    @Operation(summary = "根据条件查询菜单")
    @SaCheckPermission(value = {"center:menu:query"}, mode = SaMode.OR)
    public PageResult<MenuEntity> getMenuList(MenuQuery query) {
        List<MenuEntity> list = menuService.list(query);
        PageResult<MenuEntity> pageResult = new PageResult<>(list);
        pageResult.setTotal(query.getTotal());
        return pageResult;
    }

    @GetMapping("btnList")
    @Operation(summary = "查询菜单权限列表")
    public List<MenuEntity> getBtnList(@RequestParam("id") String id) {
        MenuEntity m = new MenuEntity();
        m.setParentId(id);
        m.setType("b");
        return menuService.list(Wrappers.query(m));
    }

    @PostMapping("addMenu")
    @Operation(summary = "新增菜单信息")
    @SaCheckPermission(value = {"center:menu:add"}, mode = SaMode.OR)
    public CommonResult addMenu(@RequestBody MenuEntity menuEntity) {
        //重新组装菜单信息表
        menuEntity.setId(String.valueOf(YitIdHelper.nextId()));
        menuEntity.setType("c");
        menuEntity.setStatus("0");
        menuService.save(menuEntity);
        return CommonResult.success();
    }

    @PostMapping("editMenu")
    @Operation(summary = "修改菜单信息")
    @SaCheckPermission(value = {"center:menu:edit"}, mode = SaMode.OR)
    public CommonResult editMenu(@RequestBody MenuEntity menuEntity) {
        //重新组装菜单信息表
        menuService.updateById(menuEntity);
        return CommonResult.success();
    }

    @PostMapping("delMenu")
    @Operation(summary = "删除菜单信息")
    @SaCheckPermission(value = {"center:menu:del"}, mode = SaMode.OR)
    public CommonResult delMenu(@RequestBody IdsModel IdsModel) {
        menuService.delMenu(IdsModel.getId());
        return CommonResult.success();
    }

    @PostMapping("addBtnMenu")
    @Operation(summary = "新增菜单权限信息")
    @SaCheckPermission(value = {"system:menu:add"}, mode = SaMode.OR)
    public CommonResult addBtnMenu(@RequestBody MenuEntity menuEntity) {
        menuEntity.setId(String.valueOf(YitIdHelper.nextId()));
        menuEntity.setStatus("0");
        menuEntity.setType("b");
        menuService.save(menuEntity);
        return CommonResult.success();

    }

    @PostMapping("editBtnMenu")
    @Operation(summary = "修改菜单权限信息")
    @SaCheckPermission(value = {"center:menu:edit"}, mode = SaMode.OR)
    public CommonResult editBtnMenu(@RequestBody MenuEntity menuEntity) {
        menuService.updateById(menuEntity);
        return CommonResult.success();
    }

    @PostMapping("delBtnMenu")
    @Operation(summary = "删除菜单权限信息")
    @SaCheckPermission(value = {"center:menu:del"}, mode = SaMode.OR)
    public CommonResult delBtnMenu(@RequestBody IdsModel idsModel) {
        menuService.delBtnMenu(idsModel.getId());
        return CommonResult.success();
    }
}
