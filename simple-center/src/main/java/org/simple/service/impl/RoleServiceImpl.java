package org.simple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.entity.Role;
import org.simple.entity.RoleMenu;
import org.simple.mapper.RoleMapper;
import org.simple.service.RoleService;
import org.simple.utils.CommonResult;
import org.simple.utils.RedomUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    @Override
    public CommonResult delRole(String id) {
        //判断是否有关联的用户，如果有关联用户则不能进行删除
        if (baseMapper.queryRoleUser(id) != 0) {
            return CommonResult.failed("存在关联用户，请先取消用户关联");
        }
        //删除角色关联的菜单信息
        baseMapper.delRoleMenu(id);
        //删除角色
        baseMapper.deleteById(id);
        return CommonResult.successNodata("删除成功");
    }

    @Override
    public CommonResult queryRoleMenu(String roleId) {
        List<String> menu = baseMapper.queryRoleMenu(roleId);
        return CommonResult.success(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult insertRoleMenus(RoleMenu roleMenu) {
        //先删除权限，在初始化
        baseMapper.delRoleMenu(roleMenu.getRole());
        List<String> menus = Arrays.asList(roleMenu.getMenu().split(","));
        String role = roleMenu.getRole();
        menus.forEach(menu -> {
            baseMapper.insetRoleMenu(RedomUtil.getRoleMenuId(), role, menu);
        });
        return CommonResult.successNodata("保存成功");
    }
}
