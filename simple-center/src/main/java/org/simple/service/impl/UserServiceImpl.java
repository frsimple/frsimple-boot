package org.simple.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.dto.UserDto;
import org.simple.entity.Menu;
import org.simple.entity.User;
import org.simple.mapper.UserMapper;
import org.simple.service.IUserService;
import org.simple.utils.CommonResult;
import org.simple.utils.RandomUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * UserServiceImpl
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public List<Tree<String>> getUserMenu(String userId) {
        List<Menu> menus = baseMapper.getUserMenu(userId);
        if (CollectionUtils.isNotEmpty(menus)) {
            TreeNodeConfig config = new TreeNodeConfig();
            return TreeUtil.build(menus, "999999", config,
                    (object, tree) -> {
                        tree.setName(object.getName());
                        tree.setId(object.getId());
                        tree.setWeight(StrUtil.isEmpty(object.getSort()) ? 0 : Integer.parseInt(object.getSort()));
                        tree.setParentId(object.getParentid());
                        tree.putExtra("component", object.getComponent());
                        tree.putExtra("path", object.getPath());
                        tree.setName(
                                object.getPath().startsWith("/") ?
                                        object.getPath().substring(1) :
                                        object.getPath());
                        tree.putExtra("meta", object.getMeta());
                        tree.putExtra("redirect", "");
                    });
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public CommonResult delUser(String userId) {
        //先删除用户关联的数据
        baseMapper.delRoleUser(userId);
        baseMapper.delUserTenant(userId);
        baseMapper.deleteById(userId);
        return CommonResult.successNodata("删除成功");
    }

    @Override
    public void insertUserTenant(String id, String tenant, String user) {
        baseMapper.insertUserTenant(id, tenant, user);
    }

    @Override
    public void insertRoleUser(String id, String role, String user) {
        baseMapper.insertRoleUser(id, role, user);
    }

    @Override
    public IPage<List<UserDto>> listAll(Page page, User user) {
        return baseMapper.listAll(page, user);
    }

    @Override
    public CommonResult updateUser(UserDto userDto) {
        User user = new User();
        user.setUpdatedate(LocalDateTime.now());
        user.setTenant(userDto.getTenant());
        user.setUsername(userDto.getUsername());
        user.setNickname(userDto.getNickname());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setId(userDto.getId());
        user.setOrgan(userDto.getOrgan());
        baseMapper.updateById(user);

        //删除用户机构关联关系,角色用户关联关系
        baseMapper.delUserTenant(userDto.getId());
        baseMapper.delRoleUser(userDto.getId());

        //重新插入用户角色和用户机构关联关系
        baseMapper.insertUserTenant(RandomUtil.getUserTenantId(), userDto.getTenant(), userDto.getId());
        String[] roles = userDto.getRoles().split(",");
        for (String role : roles) {
            baseMapper.insertRoleUser(RandomUtil.getRoleUserId(), role, userDto.getId());
        }
        return CommonResult.successNodata("修改成功");
    }
}
