package org.simple.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件操作异常封装
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileException extends Exception {

    /**
     * 异常编码
     */
    private Object errorCode;

    /**
     * 异常信息
     */
    private String errorMessage;

    /**
     * 自定义文件异常
     *
     * @param errorCode 异常编码
     */
    public FileException(Object errorCode) {
        ErrorCodesEnum codesEnum = ErrorCodesEnum.fromCode(errorCode.toString());
        if (codesEnum != null) {
            this.errorCode = codesEnum.getKey();
            this.errorMessage = codesEnum.getValue();
        } else {
            this.errorCode = ErrorCodesEnum.A400.getKey();
            this.errorMessage = ErrorCodesEnum.A400.getValue();
        }
    }

    /**
     * 自定义文件异常
     *
     * @param errorMessage 异常信息
     */
    public FileException(String errorMessage) {
        super(errorMessage);
        this.errorCode = ErrorCodesEnum.A400.getKey();
        this.errorMessage = errorMessage;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
