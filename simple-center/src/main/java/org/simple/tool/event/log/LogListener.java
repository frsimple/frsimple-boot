package org.simple.tool.event.log;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.simple.constant.CommonConst;
import org.simple.entity.Logs;
import org.simple.entity.User;
import org.simple.service.IAuthService;
import org.simple.service.ILogsService;
import org.simple.service.IUserService;
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
    private final IUserService userService;
    private final IAuthService authService;


    /**
     * 异步执行事件
     *
     * @param event 事件
     */
    @EventListener(LogEvent.class)
    public void logListener(LogEvent<LogEventArgs<String>> event) {
        try {
            LogEventArgs<String> args = (LogEventArgs<String>) event.getSource();
            String logEntity = null;
            // 从token中获取信息
            if (StringUtils.isNotBlank(authService.getCurrentUserId())) {
                User userInfo = userService.getById(authService.getCurrentUserId());
                if (userInfo.getId() != null) {
                    logEntity = JSONUtil.toJsonStr(args.getData());
                }
            }
            // token不存在信息直接记录日志
            if (logEntity == null) {
                logEntity = JSONUtil.toJsonStr(args.getData());
            }
            logsService.saveUpdate(JSONUtil.toBean(logEntity, Logs.class));
        } catch (Exception e) {
            log.error("执行错误 {}", e.getMessage());
        }
    }
}
