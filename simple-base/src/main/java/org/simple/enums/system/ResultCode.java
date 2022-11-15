package org.simple.enums.system;

import org.simple.constant.IErrorCode;

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
    FORBIDDEN(403, "没有相关权限");

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

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return message;
    }
}
