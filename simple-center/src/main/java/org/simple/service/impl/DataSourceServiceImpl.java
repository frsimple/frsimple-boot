package org.simple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.entity.DataSource;
import org.simple.mapper.DataSourceMapper;
import org.simple.service.DataSourceService;
import org.springframework.stereotype.Service;


@Service
public class DataSourceServiceImpl
        extends ServiceImpl<DataSourceMapper, DataSource>
        implements DataSourceService {
}
