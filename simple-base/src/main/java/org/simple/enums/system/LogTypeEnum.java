package org.simple.enums.system;

import java.util.Arrays;

/**
 * 登录日志类型
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/18
 */
public enum LogTypeEnum {

    /**
     * 认证
     */
    AUTH("auth", "认证"),

    /**
     * 请求
     */
    REQUEST("request", "请求"),

    /**
     * 异常
     */
    EXCEPTION("exception", "异常"),

    /**
     * 接口
     */
    INTER_FACE("interFace", "接口"),

    /**
     * 消息
     */
    MQ("mq", "消息");


    /**
     * 编号
     */
    private final String key;

    /**
     * 名称
     */
    private final String value;

    LogTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据枚举key获取枚举
     *
     * @param key 编号
     * @return 枚举
     */
    public static LogTypeEnum fromCode(String key) {
        return Arrays.stream(values()).filter(v -> v.getKey().equals(key)).findFirst().orElse(null);
    }

    /**
     * 根据枚举value获取枚举
     *
     * @param value 名称
     * @return 枚举
     */
    public static LogTypeEnum fromDesc(String value) {
        return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst().orElse(null);
    }

    /**
     * 获取枚举key
     *
     * @return 枚举key
     */
    public String getKey() {
        return key;
    }

    /**
     * 获取枚举value
     *
     * @return 枚举value
     */
    public String getValue() {
        return value;
    }
}
