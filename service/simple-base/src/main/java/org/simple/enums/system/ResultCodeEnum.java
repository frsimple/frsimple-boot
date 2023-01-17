package org.simple.enums.system;

import org.simple.constant.IErrorCode;

import java.util.Arrays;

/**
 * 异常code枚举
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */

public enum ResultCodeEnum implements IErrorCode {

    /**
     * 操作成功
     */
    SUCCESS(0, "操作成功"),

    /**
     * 操作失败
     */
    FAILED(1, "操作失败"),

    /**
     * 参数检验失败
     */
    VALIDATE_FAILED(404, "参数检验失败"),
    /**
     * 断言异常
     */
    ILLEGAL_ARGUMENT_EXCEPTION(5300, "断言异常"),

    /**
     * token为空
     **/
    NOT_TOKEN_EXCEPTION(51066, "没有访问权限"),
    /**
     * token无效
     **/
    INVALID_EXCEPTION(51067, "token无效"),
    /**
     * token过期
     **/
    TOKEN_TINEDOUT_EXCEPTION(51068, "token已过期"),
    /**
     * 用户被顶下线
     **/
    BE_REPLACED_EXCEPTION(51069, "用户被顶下线"),
    /**
     * 用户被踢下线
     **/
    KICK_OUT_EXCEPTION(51070, "用户被踢下线"),
    /**
     * 用户被踢下线
     **/
    NOT_LOGIN_EXCEPTION(51065, "当前会话未登录"),

    /**
     * 没有相关权限
     */
    FORBIDDEN(403, "没有相关权限"),

    /**
     * 没有相关权限
     */
    DB5001(5001, "存在关联的下级组织，请先删除所有下级组织"),

    /**
     * 数据链接异常
     */
    DB5002(5002, "请检查 1、连接信息 2、网络通信 3、数据库服务启动状态"),

    /**
     * 存在关联的用户，请先取消关联
     */
    DB5003(5003, "存在关联的用户，请先取消关联"),

    /**
     * 用户账户不存在
     */
    A5201(5201, "用户账户不存在"),

    /**
     * 用户账户被冻结
     */
    A5202(5202, "用户账户被冻结"),

    /**
     * 用户账户被冻结
     */
    A5203(5203, "用户账户被锁定"),

    /**
     * 验证码错误
     */
    A5204(5204, "验证码错误"),

    /**
     * 验证码已过期
     */
    A5205(5205, "验证码已过期"),

    /**
     * 用户密码错误
     */
    A5210(5210, "用户密码错误"),

    /**
     * 用户输入密码错误次数超限
     */
    A5211(5211, "用户输入密码错误次数超限");


    /**
     * 返回编码
     */
    private long code;

    /**
     * 返回信息
     */
    private String message;

    ResultCodeEnum(long code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据枚举code获取枚举
     *
     * @param code 编号
     * @return 枚举
     */
    public static ResultCodeEnum fromCode(String code) {
        return Arrays.stream(values()).filter(v -> String.valueOf(v.getCode()).equals(code)).findFirst().orElse(null);
    }

    /**
     * 根据枚举message获取枚举
     *
     * @param message 名称
     * @return 枚举
     */
    public static ResultCodeEnum fromDesc(String message) {
        return Arrays.stream(values()).filter(v -> v.getMsg().equals(message)).findFirst().orElse(null);
    }

    public long getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }
}
