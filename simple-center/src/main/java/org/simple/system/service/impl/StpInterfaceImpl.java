package org.simple.system.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import lombok.AllArgsConstructor;
import org.simple.system.service.IUserService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sa-Token的自定义权限验证扩展
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */

@Component
@AllArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final IUserService userService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return userService.getUserMenuAuth(loginId.toString());
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return userService.getUserRole(loginId.toString());
    }
}
