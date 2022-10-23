package org.simple.exception;

import org.simple.enums.system.ErrorCodesEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private Object errorCode;

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
        ErrorCodesEnum codesEnum = ErrorCodesEnum.fromCode(errorCode.toString());
        if (codesEnum != null) {
            this.errorCode = codesEnum.getKey();
            this.errorMessage = codesEnum.getValue();
        } else {
            this.errorCode = ErrorCodesEnum.A400.getKey();
            this.errorMessage = ErrorCodesEnum.A400.getValue();
        }
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
