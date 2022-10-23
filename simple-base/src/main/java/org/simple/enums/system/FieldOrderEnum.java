package org.simple.enums.system;

import java.util.Arrays;

/**
 * 排序 asc正序 desc倒序
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-7-23
 */
public enum FieldOrderEnum {
    /**
     * 正序
     */
    ASC("asc", "正序"),

    /**
     * 倒序
     */
    DESC("desc", "倒序");

    /**
     * 编号
     */
    private final String key;

    /**
     * 描述
     */
    private final String value;

    FieldOrderEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据枚举key获取枚举
     *
     * @param key 编号
     * @return 枚举
     */
    public static FieldOrderEnum fromCode(String key) {
        return Arrays.stream(values()).filter(v -> v.getKey().equals(key)).findFirst().orElse(null);
    }

    /**
     * 根据枚举value获取枚举
     *
     * @param value 名称
     * @return 枚举
     */
    public static FieldOrderEnum fromDesc(String value) {
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
