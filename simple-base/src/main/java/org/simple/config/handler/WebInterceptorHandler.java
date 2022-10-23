package org.simple.config.handler;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.simple.config.jwt.JwtProperties;
import org.simple.constant.CacheConst;
import org.simple.enums.system.ErrorCodesEnum;
import org.simple.model.ActionResult;
import org.simple.util.JwtUtil;
import org.simple.util.RedisUtil;
import org.simple.util.ServletUtil;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自定义拦截器
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/11
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class WebInterceptorHandler implements HandlerInterceptor {

    /**
     * 拦截器常量
     */
    public static final String ADD_SESSION = "/addSession";
    /**
     * jwt属性实体
     */
    private final JwtProperties jwtProperties;

    /**
     * redis工具类
     */
    private final RedisUtil redisUtil;

    /**
     * 目标方法执行前，执行
     * note：如果是true就进行下一步操作；若返回false，则证明不符合拦截条件，在失败的时候不会包含任何响应，此时需要调用对应的response返回对应响应。
     *
     * @param request  请求
     * @param response 返回
     * @param handler  句柄
     * @return 是否成功
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        log.info(DateUtil.now() + " 调用方式:" + request.getMethod() + " 调用URL:" + request.getRequestURI());
        request.setAttribute("startTime", DateUtil.current());
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }
        /* 获取token */
        String token = request.getHeader(jwtProperties.getTokenCode());
        String realToken = JwtUtil.getRealToken(token);
        JWT jwt = JWTUtil.parseToken(realToken);
        DateTime expTime = DateUtil.date(Long.parseLong(jwt.getPayload("exp").toString()));
        String userId = String.valueOf(jwt.getPayload("userId"));
        /* 检查token是否过期 */
        if (!redisUtil.hasKey(CacheConst.CACHE_TOKEN + userId)) {
            ActionResult<?> result = ActionResult.fail(ErrorCodesEnum.A402.getKey(), ErrorCodesEnum.A402.getValue());
            ServletUtil.renderString(response, JSONUtil.toJsonStr(result));
            return false;
        }
        if (StrUtil.isEmpty(realToken) || expTime.isBefore(DateTime.now())) {
            ActionResult<?> result = ActionResult.fail(ErrorCodesEnum.A402.getKey(), ErrorCodesEnum.A402.getValue());
            ServletUtil.renderString(response, JSONUtil.toJsonStr(result));
            return false;
        } else {
            tokenTimeout(userId);
        }
        return true;
    }

    /**
     * 目标方法执行完后,生成试图后，执行
     * note：可以通过ModelAndView对视图进行处理，当然ModelAndView也可以设置为 null。
     *
     * @param request      请求
     * @param response     返回
     * @param handler      句柄
     * @param modelAndView 实体和视图
     */
    @Override
    public void postHandle(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, ModelAndView modelAndView) {
        if (request.getRequestURI().contains(ADD_SESSION)) {
            HttpSession session = request.getSession();
            String name = (String) session.getAttribute("name");
            System.out.println("当前浏览器的session：" + name);
        }
    }

    /**
     * 目标方法完全执行完毕后，执行
     * note：在 DispatcherServlet 完全处理请求后被调用，通常用于记录消耗时间，也可以对一些资源进行处理。
     *
     * @param request  请求
     * @param response 返回
     * @param handler  句柄
     * @param ex       错误信息
     */
    @Override
    public void afterCompletion(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) {
        long time = System.currentTimeMillis() - (long) request.getAttribute("startTime");
        DynamicDataSourceContextHolder.clear();
        log.info("消耗总时长：" + time);
    }

    /**
     * 重新给redis中的token设置有效时间
     *
     * @param userId 用户id
     */
    private void tokenTimeout(String userId) {
        String tokenKey = CacheConst.CACHE_TOKEN + userId;
        String userInfoKey = CacheConst.CACHE_USERINFO + userId;
        long expTime = DateUtil.offsetMinute(DateUtil.date(), jwtProperties.getExpTime()).getTime();
        redisUtil.expire(tokenKey, expTime);
        redisUtil.expire(userInfoKey, expTime);
    }
}