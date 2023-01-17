package org.simple.config.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import org.simple.enums.system.ResultCodeEnum;
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
    public CommonResult  workFlowExceptionHandler(WorkFlowException ex) {
        if (ex != null) {
            return CommonResult.failed(ex.getErrorMessage());
        }
        return CommonResult.failed();
    }

    /**
     * 自定义异常捕捉处理
     *
     * @param ex 异常信息
     * @return 统一结果
     */
    @ExceptionHandler(value = CustomException.class)
    public CommonResult  customExceptionHandler(CustomException ex) {
        if (ex != null) {
            return CommonResult.failed(ex.getErrorMessage());
        }
        return CommonResult.failed();
    }

    /**
     * 文件操作异常捕捉处理
     *
     * @param ex 异常信息
     * @return 统一结果
     */
    @ExceptionHandler(value = FileException.class)
    public CommonResult fileExceptionHandler(FileException ex) {
        if (ex != null) {
            return CommonResult.failed(ex.getErrorMessage());
        }
        return CommonResult.failed();
    }


    /**
     * Sa-token未登录异常处理
     *
     * @param exception 异常
     * @return 返回
     */
    @ExceptionHandler(value = NotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult<Boolean> unauthenticatedExceptionHandler(NotLoginException exception) {
        // 判断场景值，定制化异常信息
        ResultCodeEnum resultCode;
        if (exception.getType().equals(NotLoginException.NOT_TOKEN)) {
            resultCode = org.simple.enums.system.ResultCodeEnum.NOT_TOKEN_EXCEPTION;
        } else if (exception.getType().equals(NotLoginException.INVALID_TOKEN)) {
            resultCode = org.simple.enums.system.ResultCodeEnum.INVALID_EXCEPTION;
        } else if (exception.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            resultCode = org.simple.enums.system.ResultCodeEnum.TOKEN_TINEDOUT_EXCEPTION;
        } else if (exception.getType().equals(NotLoginException.BE_REPLACED)) {
            resultCode = org.simple.enums.system.ResultCodeEnum.BE_REPLACED_EXCEPTION;
        } else if (exception.getType().equals(NotLoginException.KICK_OUT)) {
            resultCode = org.simple.enums.system.ResultCodeEnum.KICK_OUT_EXCEPTION;
        } else {
            resultCode = org.simple.enums.system.ResultCodeEnum.NOT_TOKEN_EXCEPTION;
        }
        return CommonResult.failed();
    }


    /**
     * Sa-token 接口无权返回返回信息增强
     *
     * @param exception 异常
     * @return 返回
     */
    @ExceptionHandler(value = NotPermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult<?> handleNotPermissionException(NotPermissionException exception) {
        return CommonResult.failed(ResultCodeEnum.FORBIDDEN.getMsg());
    }

    /**
     * 断言异常
     *
     * @param exception 异常
     * @return 返回
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<Boolean> illegalArgumentException(IllegalArgumentException exception) {
        return CommonResult.failed(ResultCodeEnum.ILLEGAL_ARGUMENT_EXCEPTION.getMsg());
    }
}
