package org.simple.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yitter.idgen.YitIdHelper;
import org.simple.constant.CommonConst;
import org.simple.system.dto.menu.MenuTreeDto;
import org.simple.system.dto.user.UserEntityDto;
import org.simple.system.dto.user.UserQuery;
import org.simple.system.entity.MenuEntity;
import org.simple.system.entity.UserEntity;
import org.simple.system.mapper.UserMapper;
import org.simple.system.service.IUserService;
import org.simple.utils.CommonResult;
import org.simple.utils.PageUtil;
import org.simple.utils.TreeUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserServiceImpl
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
    @Override
    public List<MenuTreeDto> getUserMenu(String userId) {
//        List<MenuEntity> menuEntities = baseMapper.getUserMenu(userId, "c");
//        if (CollectionUtils.isNotEmpty(menuEntities)) {
//            TreeNodeConfig config = new TreeNodeConfig();
//            return TreeUtil.build(menuEntities, "999999", config,
//                    (object, tree) -> {
//                        tree.setName(object.getName());
//                        tree.setId(object.getId());
//                        tree.setWeight(StrUtil.isEmpty(object.getSort()) ? 0 : Integer.parseInt(object.getSort()));
//                        tree.setParentId(object.getParentId());
//                        tree.putExtra("component", object.getComponent());
//                        tree.putExtra("path", object.getPath());
//                        tree.setName(
//                                object.getPath().startsWith("/") ?
//                                        object.getPath().substring(1) :
//                                        object.getPath());
//                        tree.putExtra("meta", object.getMeta());
//                        tree.putExtra("redirect", "");
//                    });
//        } else {
//            return new ArrayList<>();
//        }

        List<MenuEntity> menuList = baseMapper.getUserMenu(userId, "c");
        List<MenuTreeDto> menuTreeDtoList = BeanUtil.copyToList(menuList, MenuTreeDto.class);
        //List<MenuTreeDto> data = menuTreeDtoList.stream().sorted(Comparator.comparing(MenuTreeDto::getSort)).collect(Collectors.toList());
        return new TreeUtil<MenuTreeDto>().buildTree(menuTreeDtoList, "999999");
    }

    @Override
    public UserEntity getInfoByUserName(String userName) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUsername, userName);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<String> getUserMenuAuth(String userId) {
        List<MenuEntity> menuEntities = baseMapper.getUserMenu(userId, "b");
        return menuEntities.stream().map(MenuEntity::getAuthCode).collect(Collectors.toList());
    }

    @Override
    public List<String> getUserRole(String userId) {
        return baseMapper.getUserRole(userId);
    }

    @Override
    public CommonResult delUser(String userId) {
        //先删除用户关联的数据
        baseMapper.delRoleUser(userId);
        baseMapper.delUserTenant(userId);
        baseMapper.deleteById(userId);
        return CommonResult.success();
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
    public List<UserEntityDto> listAll(UserQuery query) {
        List<UserEntityDto> list = baseMapper.listAll(query);
        return query.setList(PageUtil.getListPage(query.getCurrent(), query.getPageSize(), list), list.size());
    }

    @Override
    public CommonResult updateUser(UserEntityDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setNickName(userDto.getNickName());
        userEntity.setPhone(userDto.getPhone());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setId(userDto.getId());
        userEntity.setOrgan(userDto.getOrgan());
        baseMapper.updateById(userEntity);

        //删除用户机构关联关系,角色用户关联关系
        baseMapper.delUserTenant(userDto.getId());
        baseMapper.delRoleUser(userDto.getId());

        //重新插入用户角色和用户机构关联关系
        baseMapper.insertUserTenant(String.valueOf(YitIdHelper.nextId()), userDto.getTenant(), userDto.getId());
        String[] roles = userDto.getRoles().split(CommonConst.STRING_COMMA);
        for (String role : roles) {
            baseMapper.insertRoleUser(String.valueOf(YitIdHelper.nextId()), role, userDto.getId());
        }
        return CommonResult.success();
    }
}
