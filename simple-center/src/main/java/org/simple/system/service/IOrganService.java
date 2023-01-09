package org.simple.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.dto.PageModel;
import org.simple.system.dto.organ.OrganTreeDto;
import org.simple.system.entity.OrganEntity;

import java.util.List;


/**
 * 组织管理
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
public interface IOrganService extends IService<OrganEntity> {

    List<OrganTreeDto> queryTree(PageModel pageModel);

}