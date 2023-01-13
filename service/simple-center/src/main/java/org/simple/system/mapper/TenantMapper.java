package org.simple.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.simple.system.entity.TenantEntity;

/**
 * TenantMapper
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Mapper
public interface TenantMapper extends BaseMapper<TenantEntity> {

    @Select("select count(1) from center_usertenant t where t.tenant = #{id} ")
    Integer selectCount(@Param("id") String id);
}
