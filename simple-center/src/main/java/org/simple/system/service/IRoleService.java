package org.simple.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.system.dto.role.RoleQuery;
import org.simple.system.entity.RoleEntity;
import org.simple.system.entity.RoleMenuEntity;
import org.simple.exception.CustomException;

import java.util.List;

/**
 * RoleService
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
public interface IRoleService extends IService<RoleEntity> {

    /**
     * 删除角色
     *
     * @param id 主键id
     * @return 是否成功
     * @throws CustomException 异常
     */
    Boolean delRole(String id) throws CustomException;


    /**
     * 查询角色的菜单
     *
     * @param roleId 角色id
     * @return 是否成功
     */
    List<String> queryRoleMenu(String roleId);


    /**
     * 给角色添加权限
     *
     * @param roleMenuEntity 角色信息
     * @return 是否成功
     */
    Boolean insertRoleMenus(RoleMenuEntity roleMenuEntity);

    /**
     * 查询列表
     *
     * @param query 查询参数
     * @return 数据列表
     */
    List<RoleEntity> list(RoleQuery query);
}
