package org.simple.system.tool.event.log;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simple.system.entity.LogsEntity;
import org.simple.system.service.ILogsService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 监听日志事件处理
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-12-28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogListener {
    private final ILogsService logsService;

    /**
     * 异步执行事件
     *
     * @param event 事件
     */
    @EventListener(LogEvent.class)
    public void logListener(LogEvent<LogEventArgs<String>> event) {
        try {
            LogEventArgs<String> args = (LogEventArgs<String>) event.getSource();
            String logEntity = JSONUtil.toJsonStr(args.getData());
            if (logEntity != null) {
                logsService.save(JSONUtil.toBean(logEntity, LogsEntity.class));
            }
        } catch (Exception e) {
            log.error("执行错误 {}", e.getMessage());
        }
    }
}
