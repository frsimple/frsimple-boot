package org.simple.enums.system;

import java.util.Arrays;

/**
 * 是否删除 0正常 1已删除
 *
 * @author hw_ren
 * @version v1.0
 * @since 2022-7-17
 */
public enum IsDeletedEnum {
    /**
     * 已删除
     */
    True("true", "已删除"),
    /**
     * 正常
     */
    False("false", "正常"),

    /**
     * 无
     */
    NULL("-1", "");

    /**
     * 编号
     */
    private final String key;

    /**
     * 描述
     */
    private final String value;

    IsDeletedEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据枚举key获取枚举
     *
     * @param key 编号
     * @return 枚举
     */
    public static IsDeletedEnum fromCode(String key) {
        return Arrays.stream(values()).filter(v -> v.getKey().equals(key)).findFirst().orElse(NULL);
    }

    /**
     * 根据枚举value获取枚举
     *
     * @param value 名称
     * @return 枚举
     */
    public static IsDeletedEnum fromDesc(String value) {
        return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst().orElse(NULL);
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
