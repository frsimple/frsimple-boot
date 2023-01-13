package org.simple.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.dto.PageModel;
import org.simple.system.entity.TenantEntity;

import java.util.List;

/**
 * TenantService
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
public interface ITenantService extends IService<TenantEntity> {

    Integer selectCount(String id);

    /**
     * 查询分页列表
     *
     * @param pageModel 查询参数
     * @return 列表数据
     */
    List<TenantEntity> list(PageModel pageModel);

    /**
     * 查询所有列表
     *
     * @param pageModel 查询参数
     * @return 列表数据
     */
    List<TenantEntity> allList(PageModel pageModel);
}
