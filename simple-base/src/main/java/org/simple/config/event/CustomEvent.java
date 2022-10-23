package org.simple.config.event;

import java.util.EventObject;

/**
 * 自定义事件
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-7-17
 */
public class CustomEvent extends EventObject {

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件数据
     */
    private Object data;


    /**
     * 自定义事件
     *
     * @param source 来源
     */
    public CustomEvent(Object source) {
        super(source);
    }

    /**
     * 自定义事件
     *
     * @param source    来源
     * @param eventName 事件名称
     */
    public CustomEvent(Object source, String eventName) {
        super(source);
        this.eventName = eventName;
    }

    /**
     * 自定义事件
     *
     * @param source    来源
     * @param eventName 事件名称
     * @param data      数据
     */
    public CustomEvent(Object source, String eventName, Object data) {
        super(source);
        this.eventName = eventName;
        this.data = data;
    }

    /**
     * 获取事件名称
     *
     * @return 事件名称
     */
    public String getEventName() {
        return this.eventName;
    }

    /**
     * 获取事件数据
     *
     * @return 数据
     */
    public Object getData() {
        return this.data;
    }
}
