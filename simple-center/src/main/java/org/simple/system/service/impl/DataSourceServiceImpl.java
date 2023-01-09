package org.simple.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.system.entity.DataSourceEntity;
import org.simple.system.mapper.DataSourceMapper;
import org.simple.system.service.IDataSourceService;
import org.springframework.stereotype.Service;

/**
 * DataSourceServiceImpl
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSourceEntity> implements IDataSourceService {
}
