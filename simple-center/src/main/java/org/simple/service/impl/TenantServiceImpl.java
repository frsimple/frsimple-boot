package org.simple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.entity.Tenant;
import org.simple.mapper.TenantMapper;
import org.simple.service.TenantService;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {
    @Override
    public Integer selectCount(String id) {
        return baseMapper.selectCount(id);
    }
}
