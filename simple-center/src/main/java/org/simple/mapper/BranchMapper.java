package org.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.simple.dto.OrganTreeDto;
import org.simple.entity.Branch;

import java.util.List;

/**
 * @Copyright: simple
 * @Date: 2022-08-03 21:47:58
 * @Author: frsimple
 */


public interface BranchMapper
        extends BaseMapper<Branch> {

    @Select("select id,parentid,(select t1.name from (select id,name from center_branch  union  select id,name from center_tenant )t1 where t1.id = parentid) as parentname,name,(select name from center_tenant where tenantid = id) as tenantname,tenantid," +
            "sort,createtime from center_branch  UNION  " +
            "select id,0 as parentid,name as parentname,name,name as tenantname,id as tenantid,0 as sort,createdate as createtime from center_tenant")
    public List<OrganTreeDto> queryOrganTree();

    @Select("select * from (select id,parentid,(select t1.name from (select id,name from center_branch  union  select id,name from center_tenant )t1 where t1.id = parentid) as parentname,name,(select name from center_tenant where tenantid = id) as tenantname,tenantid," +
            "sort,createtime from center_branch  UNION  " +
            "select id,0 as parentid,name as parentname,name,name as tenantname,id as tenantid,0 as sort,createdate as " +
            "createtime from center_tenant)t where  t.tenantid = #{tenantId}")
    public List<OrganTreeDto> queryOrganTreeByName(@Param("tenantId") String tenantId);

}
