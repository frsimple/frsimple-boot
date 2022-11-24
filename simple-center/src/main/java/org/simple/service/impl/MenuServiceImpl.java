package org.simple.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.entity.Menu;
import org.simple.mapper.MenuMapper;
import org.simple.service.IMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * MenuServiceImpl
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Override
    public List<Tree<String>> getTreeMenuAll() {
        List<Menu> menus = baseMapper.getTreeMenuAll();
        Menu parentM = new Menu();
        parentM.setId("999999");
        parentM.setParentid("0");
        parentM.setName("全部");
        parentM.setComponent("layout");
        menus.add(0, parentM);
        if (CollectionUtils.isNotEmpty(menus)) {
            TreeNodeConfig config = new TreeNodeConfig();
            config.setWeightKey("sort");
            List<Tree<String>> treeNodes = TreeUtil.build(menus, "0", config,
                    (object, tree) -> {
                        tree.setName(object.getName());
                        tree.setId(object.getId());
                        tree.setParentId(object.getParentid());
                        tree.setName(object.getName());
                        tree.setWeight(StrUtil.isEmpty(object.getSort()) ? 0 : Integer.valueOf(object.getSort()));
                        tree.putExtra("label", object.getName());
                        tree.putExtra("value", object.getId());
                        tree.putExtra("icon", object.getMeta() == null ? "app" : object.getMeta().get("icon"));
                        tree.putExtra("component", object.getComponent());
                    });
            return treeNodes;
        } else {
            return new ArrayList<Tree<String>>();
        }
    }

    @Override
    public List<Tree<String>> getRoleMenuAll() {
        List<Menu> menus = baseMapper.getRoleMenuAll();
        if (CollectionUtils.isNotEmpty(menus)) {
            TreeNodeConfig config = new TreeNodeConfig();
            List<Tree<String>> treeNodes = TreeUtil.build(menus, "999999", config,
                    (object, tree) -> {
                        tree.setName(object.getName());
                        tree.setId(object.getId());
                        tree.setParentId(object.getParentid());
                        tree.setName(object.getName());
                        tree.setWeight(StrUtil.isEmpty(object.getSort()) ? 0 : Integer.valueOf(object.getSort()));
                        tree.putExtra("label", object.getName());
                        tree.putExtra("value", object.getId());
                        tree.putExtra("icon", object.getMeta() == null ? "" : object.getMeta().get("icon"));
                        tree.putExtra("component", object.getComponent());
                    });
            return treeNodes;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Boolean delMenu(String id) {
        //先查询到所有子节点
        List<Menu> menuList =
                baseMapper.selectList(Wrappers.query(new Menu().setParentid(id)));
        List<String> menus = new ArrayList<>();
        menus.add(id);
        if (CollectionUtils.isNotEmpty(menuList)) {
            menuList.forEach(menu -> {
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
}
