package org.simple.tool.annotation;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.simple.entity.Logs;
import org.simple.service.IAuthService;
import org.simple.service.ILogsService;
import org.simple.tool.event.log.LogEvent;
import org.simple.tool.event.log.LogEventArgs;
import org.simple.utils.ServletUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 日志切面
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-12-28
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogRecordAspect {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ILogsService logsService;
    private final IAuthService authService;

    List<String> excludeRequestList = new ArrayList<>(Arrays.asList("org.simple.controller.AuthController.doLogin",
            "org.simple.controller.AuthController.logout",
            "org.simple.controller.AuthController.userLogout",
            "org.simple.controller.VerificationCodeController.getCode"));


    @Pointcut("execution(* org.simple.controller..*(..))")
    private void logRecord() {
    }

    /**
     * 环绕通知，在目标函数执行中执行，可控制目标函数是否执行
     *
     * @param proceedingJoinPoint 执行的切点
     * @return 无
     * @throws Throwable 错误信息
     */
    @Around("logRecord()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String method = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName();
        Object result = proceedingJoinPoint.proceed();
        if (!excludeRequestList.contains(method)) {
            long startTime = System.currentTimeMillis();
            Logs logsData = null;
            try {
                HttpServletRequest request = ServletUtil.getRequest();
                MethodSignature ms = (MethodSignature) proceedingJoinPoint.getSignature();
                Class<?> targetCls = proceedingJoinPoint.getTarget().getClass();
                //获取目标方法上的注解指定的操作名称
                Method targetMethod = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
                Operation operation = targetMethod.getAnnotation(Operation.class);
                String duration = String.valueOf((System.currentTimeMillis() - startTime));
                String summary = ObjectUtil.isNotNull(operation) ? operation.summary() : "";
                logsData = logsService.getLogData(summary, authService.getCurrentUserId(), duration, request);
                log.info("{}", JSONUtil.toJsonStr(logsData));
                applicationEventPublisher.publishEvent(new LogEvent<>(new LogEventArgs<>(JSONUtil.toJsonStr(logsData))));
                return result;
            } catch (Exception ex) {
                assert logsData != null;
                logsData.setStatus("1");
                logsData.setError(ex.getMessage());
                // 错误日志
                applicationEventPublisher.publishEvent(new LogEvent<>(new LogEventArgs<>(JSONUtil.toJsonStr(logsData))));
                throw new Exception(ex);
            }
        }
        return result;
    }
}
