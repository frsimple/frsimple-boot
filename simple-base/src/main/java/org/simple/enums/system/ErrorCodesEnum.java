package org.simple.enums.system;

import java.util.Arrays;

/**
 * 异常code枚举
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/11
 */

public enum ErrorCodesEnum {
    /**
     * 成功
     */
    A200("200", "成功"),

    /**
     * 失败
     */
    A400("400", "失败"),

    /**
     * 用户没有权限(令牌失效、用户名、密码错误、登录过期)
     */
    A401("401", "用户没有权限(令牌失效、用户名、密码错误、登录过期)"),

    /**
     * 令牌过期
     */
    A402("402", "令牌过期"),

    /**
     * 用户得到授权，但是访问是被禁止的
     */
    A403("403", "用户得到授权，但是访问是被禁止的"),

    /**
     * 用户得到授权，但是访问是被禁止的
     */
    A404("404", "访问资源不存在"),

    /**
     * 服务器发生错误
     */
    A500("500", "服务器发生错误"),

    /**
     * 用户名已存在
     */
    A0111("A0111", "用户名已存在"),

    /**
     * 密码校验失败
     */
    A0120("A0120", "密码校验失败"),

    /**
     * 密码长度不够
     */
    A0121("A0121", "密码长度不够"),

    /**
     * 密码强度不够
     */
    A0122("A0122", "密码强度不够"),

    /**
     * 用户账户不存在
     */
    A0201("A0201", "用户账户不存在"),

    /**
     * 用户账户被冻结
     */
    A0202("A0202", "用户账户被冻结"),

    /**
     * 用户密码错误
     */
    A0210("A0210", "用户密码错误"),

    /**
     * 用户输入密码错误次数超限
     */
    A0211("A0211", "用户输入密码错误次数超限"),

    /**
     * 用户验证码错误
     */
    A0240("A0240", "用户验证码错误"),

    /*======数据库相关======*/
    /**
     * 数据编号已存在
     */
    DB000("DB000", "数据编号已存在"),

    /**
     * 数据类型编码不符合标准
     */
    DB001("DB001", "数据类型编码不符合标准（请注意大小写）"),

    /**
     * 数据类型编码不符合标准
     */
    DB002("DB002", "请检查 1、连接信息 2、网络通信 3、数据库服务启动状态"),

    /**
     * 通过url找不到对应数据库
     */
    DB003("DB003", "通过url找不到对应数据库"),

    /**
     * 查询结果集为空
     */
    DB004("DB004", "查询结果集为空"),

    /**
     * 未找到对应数据库类型
     */
    DB005("DB005", "未找到对应数据库类型"),

    /**
     * 未找到对应数据类型转换
     */
    DB006("DB006", "未找到对应数据类型转换"),

    /**
     * 导入名表存在重复
     */
    DB007("DB007", "导入名表存在重复"),

    /**
     * 建表数据与当前操作数据库不匹配
     */
    DB008("DB008", "建表数据与当前操作数据库不匹配"),

    /**
     * 未找到表信息
     */
    DB009("DB009", "未找到表信息");


    /**
     * 编号
     */
    private final String key;

    /**
     * 名称
     */
    private final String value;

    ErrorCodesEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据枚举key获取枚举
     *
     * @param key 编号
     * @return 枚举
     */
    public static ErrorCodesEnum fromCode(String key) {
        return Arrays.stream(values()).filter(v -> v.getKey().equals(key)).findFirst().orElse(null);
    }

    /**
     * 根据枚举value获取枚举
     *
     * @param value 名称
     * @return 枚举
     */
    public static ErrorCodesEnum fromDesc(String value) {
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
