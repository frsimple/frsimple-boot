package org.simple.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.dto.UserDto;
import org.simple.entity.User;
import org.simple.utils.CommonResult;

import java.util.List;

/**
 * UserService
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public interface IUserService extends IService<User> {

    List<Tree<String>> getUserMenu(String userId);

    CommonResult delUser(String userId);

    void insertUserTenant(String id, String tenant, String user);

    void insertRoleUser(String id, String role, String user);

    IPage<List<UserDto>> listAll(Page page, User user);

    CommonResult updateUser(UserDto userDto);
}
