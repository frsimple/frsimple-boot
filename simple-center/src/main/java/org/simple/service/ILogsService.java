package org.simple.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.dto.LogsDto;
import org.simple.entity.Logs;

import java.util.List;

/**
 * LogsService
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public interface ILogsService extends IService<Logs> {

    IPage<List<LogsDto>> logsList(Page page, LogsDto logs);
}
