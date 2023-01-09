package org.simple.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.annotations.*;
import org.simple.system.dto.menu.MenuTreeDto;
import org.simple.system.entity.MenuEntity;

import java.util.List;

/**
 * MenuMapper
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuEntity> {
    @Select("select t1.* from center_menu t1 where t1.type = 'c' order by t1.sort asc")
    @Results({
            @Result(column = "meta", property = "meta", typeHandler = JacksonTypeHandler.class)
    })
    List<MenuTreeDto> getTreeMenuAll();

    @Select("select t1.* from center_menu t1 order by t1.sort asc")
    @Results({
            @Result(column = "meta", property = "meta", typeHandler = JacksonTypeHandler.class)
    })
    List<MenuTreeDto> getRoleMenuAll();

    void delRoleMenuByMenu(@Param("menuList") List<String> menuList);
}
