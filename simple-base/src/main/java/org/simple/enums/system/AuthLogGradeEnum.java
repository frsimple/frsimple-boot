package org.simple.enums.system;

import java.util.Arrays;

/**
 * 登录日志类型
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/18
 */
public enum AuthLogGradeEnum {

    /**
     * 登录
     */
    LOGIN("login", "登录"),

    /**
     * 登出
     */
    LOGOUT("logout", "登出"),

    /**
     * 修改密码
     */
    CHANGE_PASSWORD("changePassword", "修改密码");


    /**
     * 编号
     */
    private final String key;

    /**
     * 名称
     */
    private final String value;

    AuthLogGradeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据枚举key获取枚举
     *
     * @param key 编号
     * @return 枚举
     */
    public static AuthLogGradeEnum fromCode(String key) {
        return Arrays.stream(values()).filter(v -> v.getKey().equals(key)).findFirst().orElse(null);
    }

    /**
     * 根据枚举value获取枚举
     *
     * @param value 名称
     * @return 枚举
     */
    public static AuthLogGradeEnum fromDesc(String value) {
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
