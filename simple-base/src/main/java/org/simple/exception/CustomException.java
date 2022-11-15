package org.simple.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.enums.system.ResultCode;

/**
 * 自定义异常封装
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomException extends Exception {

    /**
     * 异常编码
     */
    private ResultCode errorCode;

    /**
     * 异常信息
     */
    private String errorMessage;

    /**
     * 自定义异常
     *
     * @param errorCode 异常编码
     */
    public CustomException(Object errorCode) {
        ResultCode codesEnum = ResultCode.fromCode(Long.parseLong(errorCode.toString()));
        if (codesEnum != null) {
            this.errorCode = codesEnum;
            this.errorMessage = codesEnum.getMsg();
        } else {
            this.errorCode = ResultCode.FAILED;
            this.errorMessage = ResultCode.FAILED.getMsg();
        }
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
