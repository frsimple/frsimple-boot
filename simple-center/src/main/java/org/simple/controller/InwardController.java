package org.simple.controller;


import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.simple.entity.Logs;
import org.simple.service.ILogsService;
import org.simple.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/center/inward")
@Tag(name = "inward", description = "日志处理")
public class InwardController {

    @Autowired
    private ILogsService logsService;

    @PostMapping("saveLogs")
    public Boolean saveSimpleLog(@RequestBody org.simple.dto.Logs centerLogs) {
        Logs log = new Logs();
        BeanUtil.copyProperties(centerLogs, log);
        System.out.println("-----全局日志处理start------");
        log.setId(RandomUtil.getLogsId());
        log.setCreatetime(LocalDateTime.now());
        System.out.println("-----全局日志处理end------");
        return logsService.save(log);
    }
}
