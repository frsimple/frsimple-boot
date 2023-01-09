package org.simple.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yitter.idgen.YitIdHelper;
import org.simple.system.dto.role.RoleQuery;
import org.simple.system.entity.RoleEntity;
import org.simple.system.entity.RoleMenuEntity;
import org.simple.enums.system.ResultCodeEnum;
import org.simple.exception.CustomException;
import org.simple.system.mapper.RoleMapper;
import org.simple.system.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * RoleServiceImpl
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements IRoleService {

    @Override
    public Boolean delRole(String id) throws CustomException {
        //判断是否有关联的用户，如果有关联用户则不能进行删除
        if (baseMapper.queryRoleUser(id) != 0) {
            throw new CustomException(ResultCodeEnum.DB5003.getCode());
        }
        //删除角色关联的菜单信息
        baseMapper.delRoleMenu(id);
        //删除角色
        baseMapper.deleteById(id);
        return true;
    }

    @Override
    public List<String> queryRoleMenu(String roleId) {
        List<String> menu = baseMapper.queryRoleMenu(roleId);
        return menu;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertRoleMenus(RoleMenuEntity roleMenuEntity) {
        //先删除权限，在初始化
        baseMapper.delRoleMenu(roleMenuEntity.getRole());
        List<String> menus = Arrays.asList(roleMenuEntity.getMenu().split(","));
        String role = roleMenuEntity.getRole();
        menus.forEach(menu -> baseMapper.insetRoleMenu(String.valueOf(YitIdHelper.nextId()), role, menu));
        return true;
    }

    @Override
    public List<RoleEntity> list(RoleQuery query) {
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(query.getName())) {
            queryWrapper.like(RoleEntity::getName, query.getName());
        }
        Page<RoleEntity> page = new Page<>(query.getCurrent(), query.getPageSize());
        IPage<RoleEntity> iPage = this.page(page, queryWrapper);
        return query.setList(iPage.getRecords(), page.getTotal());
    }
}
