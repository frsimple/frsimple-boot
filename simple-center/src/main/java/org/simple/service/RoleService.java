package org.simple.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.center.entity.Role;
import org.simple.center.entity.RoleMenu;
import org.simple.common.utils.CommonResult;
import org.simple.entity.Role;
import org.simple.entity.RoleMenu;
import org.simple.utils.CommonResult;

public interface RoleService extends IService<Role> {

    CommonResult delRole(String id);

    CommonResult queryRoleMenu(String roleId);

    CommonResult insertRoleMenus(RoleMenu roleMenu);
}
