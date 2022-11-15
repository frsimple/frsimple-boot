package org.simple.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.entity.Tenant;

/**
 * TenantService
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public interface TenantService extends IService<Tenant> {

    Integer  selectCount(String id);
}
