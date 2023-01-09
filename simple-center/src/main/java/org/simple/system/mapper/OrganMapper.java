package org.simple.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.simple.system.dto.organ.OrganTreeDto;
import org.simple.system.entity.OrganEntity;

import java.util.List;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Mapper
public interface OrganMapper extends BaseMapper<OrganEntity> {

    /**
     * 查询组织树
     *
     * @return 查询组织树
     */
    @Select("select id,parent_id,(select t1.name from (select id,name from center_organ union select id,name from center_tenant )t1 where t1.id = parent_id) as parent_name,name,(select name from center_tenant where tenant_id = id) as tenant_name,tenant_id," +
            "sort,create_time from center_organ  UNION  " +
            "select id,0 as parent_id,name as parent_name,name,name as tenant_name,id as tenant_id,0 as sort,create_time from center_tenant")
    List<OrganTreeDto> queryOrganTree();

    /**
     * 根据条件查询组织树
     *
     * @param tenantId 租户id
     * @return 组织树
     */
    @Select("select * from (select id,parent_id,(select t1.name from (select id,name from center_organ  union  select id,name from center_tenant )t1 where t1.id = parent_id) as parent_name,name,(select name from center_tenant where tenant_id = id) as tenant_name,tenant_id," +
            "sort,create_time from center_organ  UNION  " +
            "select id,0 as parent_id,name as parent_name,name,name as tenant_name,id as tenant_id,0 as sort_index,create_time from center_tenant)t where  t.tenant_id = #{tenantId}")
    List<OrganTreeDto> queryOrganTreeByName(@Param("tenantId") String tenantId);

}
