package org.simple.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.dto.LogsDto;
import org.simple.entity.Logs;
import org.simple.mapper.LogsMapper;
import org.simple.service.LogsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LogsServiceImpl
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class LogsServiceImpl extends ServiceImpl<LogsMapper, Logs> implements LogsService {

    @Override
    public IPage<List<LogsDto>> logsList(Page page, LogsDto logs) {
        return baseMapper.logsList(page, logs);
    }
}
