package org.simple.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.simple.system.entity.RoleEntity;

import java.util.List;

/**
 * RoleMapper
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {
    @Delete("delete from center_roleuser where role = #{id}")
    void delRoleUser(@Param("id") String id);

    @Delete("delete from center_rolemenu where role = #{id}")
    void delRoleMenu(@Param("id") String id);

    @Select("select count(1) from center_roleuser where role = #{id}")
    Integer queryRoleUser(@Param("id") String id);

    @Select("select t1.id from center_rolemenu t join center_menu t1 on t1.id = t.menu" +
            " where t.role = #{roleId}  order by t1.sort asc")
    List<String> queryRoleMenu(@Param("roleId") String roleId);

    @Insert("insert into center_rolemenu(id,role,menu) values(#{id},#{role},#{menu})")
    void insetRoleMenu(@Param("id") String id, @Param("role") String role, @Param("menu") String menu);
}
