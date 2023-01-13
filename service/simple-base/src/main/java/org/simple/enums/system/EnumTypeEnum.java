package org.simple.enums.system;

import java.util.Arrays;

/**
 * 枚举类型
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/9/20
 */
public enum EnumTypeEnum {

    /**
     * 登录
     */
    TABLE("table", "表枚举"),

    /**
     * 登出
     */
    NORMAL("normal", "普通枚举");


    /**
     * 编号
     */
    private final String key;

    /**
     * 名称
     */
    private final String value;

    EnumTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据枚举key获取枚举
     *
     * @param key 编号
     * @return 枚举
     */
    public static EnumTypeEnum fromCode(String key) {
        return Arrays.stream(values()).filter(v -> v.getKey().equals(key)).findFirst().orElse(null);
    }

    /**
     * 根据枚举value获取枚举
     *
     * @param value 名称
     * @return 枚举
     */
    public static EnumTypeEnum fromDesc(String value) {
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
