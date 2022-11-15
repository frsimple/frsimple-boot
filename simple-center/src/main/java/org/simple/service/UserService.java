package org.simple.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.center.dto.UserDto;
import org.simple.center.entity.User;
import org.simple.common.utils.CommonResult;
import org.simple.dto.UserDto;
import org.simple.entity.User;
import org.simple.utils.CommonResult;

import java.util.List;

public interface UserService extends IService<User> {

    List<Tree<String>> getUserMenu(String userId);

    CommonResult delUser(String userId);

    void insertUserTenant(String id, String tenant, String user);

    void insertRoleUser(String id, String role, String user);

    IPage<List<UserDto>> listAll(Page page, User user);

    CommonResult updateUser(UserDto userDto);
}
