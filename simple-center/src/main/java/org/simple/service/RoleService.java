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
public interface RoleService extends IService<Role> {

    CommonResult delRole(String id);

    CommonResult queryRoleMenu(String roleId);

    CommonResult insertRoleMenus(RoleMenu roleMenu);
}
