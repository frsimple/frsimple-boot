package org.simple.config.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import org.simple.enums.system.ResultCode;
import org.simple.exception.CustomException;
import org.simple.exception.FileException;
import org.simple.exception.WorkFlowException;
import org.simple.utils.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/11
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 流程异常捕捉处理
     *
     * @param ex 异常信息
     * @return 统一结果
     */
    @ExceptionHandler(value = WorkFlowException.class)
    public CommonResult<?> workFlowExceptionHandler(WorkFlowException ex) {
        CommonResult<?> commonResult;
        if (ex != null) {
            commonResult = CommonResult.failed(ex.getErrorCode(), ex.getErrorMessage());
            return commonResult;
        }
        return CommonResult.failed(ResultCode.FAILED, "未知异常");
    }

    /**
     * 自定义异常捕捉处理
     *
     * @param ex 异常信息
     * @return 统一结果
     */
    @ExceptionHandler(value = CustomException.class)
    public CommonResult<?> customExceptionHandler(CustomException ex) {
        CommonResult<?> commonResult;
        if (ex != null) {
            commonResult = CommonResult.failed(ex.getErrorCode(), ex.getErrorMessage());
            return commonResult;
        }
        return CommonResult.failed(ResultCode.FAILED, "未知异常");
    }

    /**
     * 文件操作异常捕捉处理
     *
     * @param ex 异常信息
     * @return 统一结果
     */
    @ExceptionHandler(value = FileException.class)
    public CommonResult<?> fileExceptionHandler(FileException ex) {
        CommonResult<?> commonResult;
        if (ex != null) {
            commonResult = CommonResult.failed(ex.getErrorCode(), ex.getErrorMessage());
            return commonResult;
        }
        return CommonResult.failed(ResultCode.FAILED, "未知异常");
    }


    /**
     * Sa-token未登录异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = NotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult<Boolean> unauthenticatedExceptionHandler(NotLoginException exception) {
        // 判断场景值，定制化异常信息
        ResultCode ResultCode = null;
        if (exception.getType().equals(NotLoginException.NOT_TOKEN)) {
            ResultCode = org.simple.enums.system.ResultCode.NOT_TOKEN_EXCEPTION;
        } else if (exception.getType().equals(NotLoginException.INVALID_TOKEN)) {
            ResultCode = org.simple.enums.system.ResultCode.INVALID_EXCEPTION;
        } else if (exception.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            ResultCode = org.simple.enums.system.ResultCode.TOKEN_TINEDOUT_EXCEPTION;
        } else if (exception.getType().equals(NotLoginException.BE_REPLACED)) {
            ResultCode = org.simple.enums.system.ResultCode.BE_REPLACED_EXCEPTION;
        } else if (exception.getType().equals(NotLoginException.KICK_OUT)) {
            ResultCode = org.simple.enums.system.ResultCode.KICK_OUT_EXCEPTION;
        } else {
            ResultCode = org.simple.enums.system.ResultCode.NOT_TOKEN_EXCEPTION;
        }
        return CommonResult.failed(ResultCode);
    }


    /**
     * Sa-token 接口无权返回返回信息增强
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = NotPermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult handleNotPermissionException(NotPermissionException exception) {
        return CommonResult.failed(ResultCode.FORBIDDEN);
    }

    /**
     * 断言异常
     *
     * @param illegalArgumentException
     * @return
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<Boolean> illegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return CommonResult.failed(ResultCode.ILLEGAL_ARGUMENT_EXCEPTION, illegalArgumentException.getMessage());
    }
}
