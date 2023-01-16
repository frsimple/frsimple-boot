package org.simple.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.dto.PageResult;
import org.simple.system.dto.log.LogsQuery;
import org.simple.system.entity.LogsEntity;
import org.simple.system.service.ILogsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 日志列表
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/system/logs")
@Tag(name = "logs", description = "日志列表")
public class LogsController {
    private final ILogsService logsService;

    @GetMapping("list")
    @Operation(summary = "查询日志列表")
    public PageResult<LogsEntity> logsList(LogsQuery query) {
        List<LogsEntity> list = logsService.logsList(query);
        PageResult<LogsEntity> pageResult = new PageResult<>(list);
        pageResult.setTotal(query.getTotal());
        return pageResult;
    }
}
