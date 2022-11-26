package org.simple.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.simple.dto.LogsDto;
import org.simple.service.ILogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 日志列表
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@RequestMapping("/center/logs")
@Tag(name = "logs", description = "日志列表")
public class LogsController {

    @Autowired
    private ILogsService logsService;

    @GetMapping("list")
    public IPage<List<LogsDto>> logsList(Page page, LogsDto logs) {
        return logsService.logsList(page, logs);
    }
}
