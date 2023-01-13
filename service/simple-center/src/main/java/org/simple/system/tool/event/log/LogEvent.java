package org.simple.system.tool.event.log;

import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-12-28
 */
public class LogEvent<T> extends ApplicationEvent {

    public LogEvent(T source) {
        // todo LogEvent非必要 , 可以直接使用ApplicationEvent
        super(source);
    }

}
