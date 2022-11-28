package org.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.simple.dto.UserDto;
import org.simple.entity.Menu;
import org.simple.entity.User;

import java.util.List;

/**
 * UserMapper
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询当前用户菜单
     *
     * @param userId 用户id
     * @return 菜单信息
     */
    @Select("select t1.* from center_rolemenu t join center_menu t1 on t1.id = t.menu" +
            " where t.role in ( select  role from center_roleuser where  " +
            "user  = #{userId}) and t1.type =#{menuType} order by t1.sort asc")
    @Results({
            @Result(column = "meta", property = "meta", typeHandler = JacksonTypeHandler.class)
    })
    List<Menu> getUserMenu(@Param("userId") String userId, @Param("menuType") String menuType);

    /**
     * 查询当前用户角色
     *
     * @param userId 用户id
     * @return 角色信息
     */
    @Select("select code from center_role t join center_roleuser t1 on t1.role = t.id" +
            " where t1.user= #{userId}")
    @Results({
            @Result(column = "meta", property = "meta", typeHandler = JacksonTypeHandler.class)
    })
    List<String> getUserRole(@Param("userId") String userId);


    @Delete("delete from center_roleuser where user = #{userId}")
    void delRoleUser(@Param("userId") String userId);

    @Delete("delete from center_usertenant where user = #{userId}")
    void delUserTenant(@Param("userId") String userId);

    @Insert("insert into center_usertenant(id,tenant,user) values(#{id},#{tenant},#{user})")
    void insertUserTenant(@Param("id") String id, @Param("tenant") String tenant, @Param("user") String user);

    @Insert("insert into center_roleuser(id,role,user) values(#{id},#{role},#{user})")
    void insertRoleUser(@Param("id") String id, @Param("role") String role, @Param("user") String user);

    IPage<List<UserDto>> listAll(Page page, @Param("user") User user);

}
