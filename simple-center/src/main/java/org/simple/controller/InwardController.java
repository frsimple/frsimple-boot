package org.simple.controller;


import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.entity.Logs;
import org.simple.service.LogsService;
import org.simple.utils.CommonResult;
import org.simple.utils.RedomUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 日志处理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@AllArgsConstructor
@RequestMapping("/inward")
@Tag(name = "inward", description = "日志处理")
public class InwardController {
    private final LogsService centerLogsService;

    @PostMapping("saveLogs")
    public CommonResult saveSimpleLog(@RequestBody org.simple.dto.Logs centerLogs) {
        Logs log = new Logs();
        BeanUtil.copyProperties(centerLogs, log);
        System.out.println("-----全局日志处理start------");
        log.setId(RedomUtil.getLogsId());
        log.setCreatetime(LocalDateTime.now());
        centerLogsService.save(log);
        System.out.println("-----全局日志处理end------");
        return CommonResult.success("成功");
    }

}
