package org.simple.enums.system;

import java.util.Arrays;

/**
 * 菜单类型 1:菜单 2:资源
 *
 * @author hw_ren
 * @version v1.0
 * @since 2022-7-17
 */
public enum PermissionTypeEnum {
    /**
     * 1:菜单
     */
    MENU(1, "菜单"),

    /**
     * 2:资源
     */
    PERMISSION(2, "资源"),

    /**
     * 无
     */
    NULL(-1, "");

    /**
     * 编号
     */
    private final Integer key;

    /**
     * 描述
     */
    private final String value;

    PermissionTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据枚举key获取枚举
     *
     * @param key 编号
     * @return 枚举
     */
    public static PermissionTypeEnum fromCode(Integer key) {
        return Arrays.stream(values()).filter(v -> v.getKey().equals(key)).findFirst().orElse(NULL);
    }

    /**
     * 根据枚举value获取枚举
     *
     * @param value 名称
     * @return 枚举
     */
    public static PermissionTypeEnum fromDesc(String value) {
        return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst().orElse(NULL);
    }

    /**
     * 获取枚举key
     *
     * @return 枚举key
     */
    public Integer getKey() {
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
