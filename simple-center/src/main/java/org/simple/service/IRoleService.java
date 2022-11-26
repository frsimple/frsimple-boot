package org.simple.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.entity.Role;
import org.simple.entity.RoleMenu;
import org.simple.utils.CommonResult;

/**
 * RoleService
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public interface IRoleService extends IService<Role> {

    /**
     * 删除角色
     *
     * @param id 主键id
     * @return 是否成功
     */
    CommonResult delRole(String id);


    /**
     * 查询角色的菜单
     *
     * @param roleId 角色id
     * @return 是否成功
     */
    CommonResult queryRoleMenu(String roleId);


    /**
     * 给角色添加权限
     *
     * @param roleMenu 角色信息
     * @return 是否成功
     */
    CommonResult insertRoleMenus(RoleMenu roleMenu);
}
