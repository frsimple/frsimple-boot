package org.simple.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.enums.system.ResultCodeEnum;

/**
 * 流程异常封装
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkFlowException extends Exception {

    /**
     * 异常编码
     */
    private String errorCode;

    /**
     * 异常信息
     */
    private String errorMessage;


    /**
     * 自定义流程异常
     *
     * @param errorCode 异常编码
     */
    public WorkFlowException(Object errorCode) {
        ResultCodeEnum codesEnum = ResultCodeEnum.fromCode(errorCode.toString());
        if (codesEnum != null) {
            this.errorCode = String.valueOf(codesEnum.getCode());
            this.errorMessage = codesEnum.getMsg();
        } else {
            this.errorCode = String.valueOf(ResultCodeEnum.FAILED.getCode());
            this.errorMessage = ResultCodeEnum.FAILED.getMsg();
        }
    }

    /**
     * 自定义流程异常
     *
     * @param errorMessage 异常信息
     */
    public WorkFlowException(String errorMessage) {
        super(errorMessage);
        this.errorCode = String.valueOf(ResultCodeEnum.FAILED.getCode());
        this.errorMessage = errorMessage;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
