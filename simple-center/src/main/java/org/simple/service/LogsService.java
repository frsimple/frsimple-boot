package org.simple.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.center.dto.LogsDto;
import org.simple.center.entity.Logs;
import org.simple.dto.LogsDto;
import org.simple.entity.Logs;

import java.util.List;

public interface LogsService extends IService<Logs> {

    IPage<List<LogsDto>> logsList(Page page, LogsDto logs);
}
