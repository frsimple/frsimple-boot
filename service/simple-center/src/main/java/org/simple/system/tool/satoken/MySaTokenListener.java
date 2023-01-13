package org.simple.system.tool.satoken;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.simple.system.controller.AuthController;
import org.simple.system.dto.auth.LoginParam;
import org.simple.system.entity.LogsEntity;
import org.simple.system.service.ILogsService;
import org.simple.utils.ServletUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 自定义侦听器的实现
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-12-28
 */
@Component
@AllArgsConstructor
public class MySaTokenListener implements SaTokenListener {

    private final ILogsService logsService;

    /**
     * 每次登录时触发
     */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel saLoginModel) {
        try {
            long startTime = System.currentTimeMillis();
            Class<?> targetCls = AuthController.class;
            Method targetMethod = targetCls.getDeclaredMethod("doLogin", LoginParam.class);
            saveLogData(loginId, targetMethod, startTime);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存日志
     *
     * @param loginId      登陆人id
     * @param targetMethod 操作方法
     * @param startTime    开始时间
     */
    private void saveLogData(Object loginId, Method targetMethod, long startTime) {
        HttpServletRequest request = ServletUtil.getRequest();
        Operation operation = targetMethod.getAnnotation(Operation.class);
        String duration = String.valueOf((System.currentTimeMillis() - startTime));
        String summary = ObjectUtil.isNotNull(operation) ? operation.summary() : "";
        LogsEntity logsData = logsService.getLogData(summary, loginId.toString(), duration, request);
        logsService.save(logsData);
    }

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        try {
            long startTime = System.currentTimeMillis();
            Class<?> targetCls = AuthController.class;
            Method targetMethod = targetCls.getDeclaredMethod("logout");
            saveLogData(loginId, targetMethod, startTime);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 每次被踢下线时触发
     */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        try {
            long startTime = System.currentTimeMillis();
            Class<?> targetCls = AuthController.class;
            Method targetMethod = targetCls.getDeclaredMethod("userLogout", String.class);
            saveLogData(loginId, targetMethod, startTime);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 每次被顶下线时触发
     */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        System.out.println("---------- 自定义侦听器实现 doReplaced");
    }

    /**
     * 每次被封禁时触发
     */
    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {
        System.out.println("---------- 自定义侦听器实现 doDisable");
    }

    /**
     * 每次被解封时触发
     */
    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {
        System.out.println("---------- 自定义侦听器实现 doUntieDisable");
    }

    /**
     * 每次二级认证时触发
     */
    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {
        System.out.println("---------- 自定义侦听器实现 doOpenSafe");
    }

    /**
     * 每次退出二级认证时触发
     */
    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {
        System.out.println("---------- 自定义侦听器实现 doCloseSafe");
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCreateSession(String id) {
        System.out.println("---------- 自定义侦听器实现 doCreateSession");
    }

    /**
     * 每次注销Session时触发
     */
    @Override
    public void doLogoutSession(String id) {
        System.out.println("---------- 自定义侦听器实现 doLogoutSession");
    }

    /**
     * 每次Token续期时触发
     */
    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {
        System.out.println("---------- 自定义侦听器实现 doRenewTimeout");
    }
}
