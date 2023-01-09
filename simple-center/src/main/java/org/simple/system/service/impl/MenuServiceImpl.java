package org.simple.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.system.dto.menu.MenuQuery;
import org.simple.system.dto.menu.MenuTreeDto;
import org.simple.system.entity.MenuEntity;
import org.simple.system.mapper.MenuMapper;
import org.simple.system.service.IMenuService;
import org.simple.utils.TreeUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MenuServiceImpl
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements IMenuService {
    @Override
    public List<MenuTreeDto> getTreeMenuAll() {
        List<MenuTreeDto> menuList = baseMapper.getTreeMenuAll();
        MenuTreeDto parentM = new MenuTreeDto();
        parentM.setId("999999");
        parentM.setParentId("0");
        parentM.setName("全部");
        parentM.setComponent("layout");
        parentM.setSort("0");
        menuList.add(0, parentM);
        menuList.forEach(
                (f) -> {
                    f.setLabel(f.getName());
                    f.setValue(f.getId());
                    f.setIcon(f.getMeta() == null ? "app" : f.getMeta().get("icon").toString());
                }
        );
        List<MenuTreeDto> data = menuList.stream().sorted(Comparator.comparing(MenuTreeDto::getSort)).collect(Collectors.toList());
        return new TreeUtil<MenuTreeDto>().buildTree(data);
    }

    @Override
    public List<MenuTreeDto> getRoleMenuAll() {
        List<MenuTreeDto> menuList = baseMapper.getRoleMenuAll();
        menuList.forEach(
                (f) -> {
                    f.setLabel(f.getName());
                    f.setValue(f.getId());
                    f.setIcon(f.getMeta() == null ? "app" : f.getMeta().get("icon").toString());
                }
        );
        List<MenuTreeDto> data = menuList.stream().sorted(Comparator.comparing(MenuTreeDto::getSort)).collect(Collectors.toList());
        return new TreeUtil<MenuTreeDto>().buildTree(data, "999999");
    }

    @Override
    public Boolean delMenu(String id) {
        //先查询到所有子节点
        LambdaQueryWrapper<MenuEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuEntity::getParentId, id);
        List<MenuEntity> menuEntityList = baseMapper.selectList(queryWrapper);
        List<String> menus = new ArrayList<>();
        menus.add(id);
        if (CollectionUtils.isNotEmpty(menuEntityList)) {
            menuEntityList.forEach(menu -> {
                menus.add(menu.getId());
            });
        }
        baseMapper.delRoleMenuByMenu(menus);
        //删除所有子节点数据
        baseMapper.deleteBatchIds(menus);
        return true;
    }

    @Override
    public Boolean delBtnMenu(String id) {
        List<String> menus = new ArrayList<>();
        menus.add(id);
        baseMapper.delRoleMenuByMenu(menus);
        //删除所有子节点数据
        baseMapper.deleteBatchIds(menus);
        return true;
    }

    /**
     * 查询列表
     *
     * @param query 分页参数
     * @return 数据列表
     */
    @Override
    public List<MenuEntity> list(MenuQuery query) {
        LambdaQueryWrapper<MenuEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(query.getName())) {
            queryWrapper.like(MenuEntity::getName, query.getName());
        }
        if (StrUtil.isNotEmpty(query.getParentId())) {
            queryWrapper.like(MenuEntity::getParentId, query.getParentId());
        }
        if (StrUtil.isNotEmpty(query.getType())) {
            queryWrapper.like(MenuEntity::getType, query.getType());
        }
        Page<MenuEntity> page = new Page<>(query.getCurrent(), query.getPageSize());
        IPage<MenuEntity> iPage = this.page(page, queryWrapper);
        return query.setList(iPage.getRecords(), page.getTotal());
    }
}
