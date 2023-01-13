package org.simple.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.dto.PageModel;
import org.simple.system.entity.TenantEntity;
import org.simple.system.mapper.TenantMapper;
import org.simple.system.service.ITenantService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TenantServiceImpl
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, TenantEntity> implements ITenantService {
    @Override
    public Integer selectCount(String id) {
        return baseMapper.selectCount(id);
    }

    /**
     * 查询列表
     *
     * @param pageModel 查询参数
     * @return 列表数据
     */
    @Override
    public List<TenantEntity> list(PageModel pageModel) {
        LambdaQueryWrapper<TenantEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(pageModel.getSearchValue())) {
            queryWrapper.like(TenantEntity::getName, pageModel.getSearchValue());
        }
        Page<TenantEntity> page = new Page<>(pageModel.getCurrent(), pageModel.getPageSize());
        IPage<TenantEntity> iPage = this.page(page, queryWrapper);
        return pageModel.setList(iPage.getRecords(), page.getTotal());
    }

    @Override
    public List<TenantEntity> allList(PageModel pageModel) {
        LambdaQueryWrapper<TenantEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(pageModel.getSearchValue())) {
            queryWrapper.like(TenantEntity::getName, pageModel.getSearchValue());
        }
        return this.list(queryWrapper);
    }
}
