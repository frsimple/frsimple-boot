package org.simple.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.dto.LogsDto;
import org.simple.service.LogsService;
import org.simple.utils.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志列表
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@AllArgsConstructor
@RequestMapping("/logs")
@Tag(name = "logs", description = "日志列表")
public class LogsController {
    private final LogsService logsService;

    @GetMapping("list")
    public CommonResult logsList(Page page, LogsDto logs) {
        return CommonResult.success(logsService.logsList(page, logs));
    }

}
