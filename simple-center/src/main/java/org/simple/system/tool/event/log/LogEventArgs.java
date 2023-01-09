package org.simple.system.tool.event.log;

import lombok.Data;

import java.io.Serializable;

/**
 * 日志事件参数实体
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-12-28
 */
@Data
public class LogEventArgs<T> implements Serializable {
    private T data;

    public LogEventArgs(T data) {
        this.data = data;
    }
}
