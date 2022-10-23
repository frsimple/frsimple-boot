package org.simple.enums.system;

import java.util.Arrays;

/**
 * 多数据库枚举
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/7
 */
public enum ConnName {

    /**
     * 基础数据库
     */
    BASE("base", "基础"),

    /**
     * 配置
     */
    CONFIG("config", "配置"),

    /**
     * 流程
     */
    WORKFLOW("workflow", "流程"),

    /**
     * 项目
     */
    PROJECT("project", "项目库");

    /**
     * 编号
     */
    private final String key;

    /**
     * 名称
     */
    private final String value;

    ConnName(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据枚举key获取枚举
     *
     * @param key 编号
     * @return 枚举
     */
    public static ConnName fromCode(String key) {
        return Arrays.stream(values()).filter(v -> v.getKey().equals(key)).findFirst().orElse(null);
    }

    /**
     * 根据枚举value获取枚举
     *
     * @param value 名称
     * @return 枚举
     */
    public static ConnName fromDesc(String value) {
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
