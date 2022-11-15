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

public enum ResultCode implements IErrorCode {

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
     * 暂未登录或token已经过期
     */
    UNAUTHORIZED(401, "暂未登录或token已经过期"),

    /**
     * 没有相关权限
     */
    FORBIDDEN(403, "没有相关权限"),

    /**
     * 没有相关权限
     */
    DB001(001, "存在关联的下级组织，请先删除所有下级组织"),

    /**
     * 数据链接异常
     */
    DB002(002, "请检查 1、连接信息 2、网络通信 3、数据库服务启动状态");

    /**
     * 返回编码
     */
    private long code;

    /**
     * 返回信息
     */
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据枚举code获取枚举
     *
     * @param code 编号
     * @return 枚举
     */
    public static ResultCode fromCode(long code) {
        return Arrays.stream(values()).filter(v -> v.getCode() == code).findFirst().orElse(null);
    }

    /**
     * 根据枚举message获取枚举
     *
     * @param message 名称
     * @return 枚举
     */
    public static ResultCode fromDesc(String message) {
        return Arrays.stream(values()).filter(v -> v.getMsg().equals(message)).findFirst().orElse(null);
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return message;
    }
}
