package org.simple.config.handler;

import org.simple.exception.CustomException;
import org.simple.exception.FileException;
import org.simple.exception.WorkFlowException;
import org.simple.model.ActionResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ActionResult<?> workFlowExceptionHandler(WorkFlowException ex) {
        ActionResult<?> actionResult;
        if (ex != null) {
            actionResult = ActionResult.fail(ex.getErrorCode().toString(), ex.getErrorMessage());
            return actionResult;
        }
        return ActionResult.fail("0", "未知异常");
    }

    /**
     * 自定义异常捕捉处理
     *
     * @param ex 异常信息
     * @return 统一结果
     */
    @ExceptionHandler(value = CustomException.class)
    public ActionResult<?> customExceptionHandler(CustomException ex) {
        ActionResult<?> actionResult;
        if (ex != null) {
            actionResult = ActionResult.fail(ex.getErrorCode().toString(), ex.getErrorMessage());
            return actionResult;
        }
        return ActionResult.fail("0", "未知异常");
    }


    /**
     * 文件操作异常捕捉处理
     *
     * @param ex 异常信息
     * @return 统一结果
     */
    @ExceptionHandler(value = FileException.class)
    public ActionResult<?> fileExceptionHandler(FileException ex) {
        ActionResult<?> actionResult;
        if (ex != null) {
            actionResult = ActionResult.fail(ex.getErrorCode().toString(), ex.getErrorMessage());
            return actionResult;
        }
        return ActionResult.fail("0", "未知异常");
    }
}
