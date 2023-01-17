package org.simple.config.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.simple.utils.CommonResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Nonnull;

/**
 * 统一请求返回
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/9
 */
@RestControllerAdvice(annotations = RestController.class)
public class GlobalResultHandler implements ResponseBodyAdvice<Object> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 支持的范围
     *
     * @param methodParameter swagger方法
     * @param aClass          http消息转化类
     */
    @Override
    public boolean supports(MethodParameter methodParameter, @Nonnull Class<? extends HttpMessageConverter<?>> aClass) {
        /* 排除swagger自带的内容 */
        return !methodParameter.getDeclaringClass().getName().contains("springdoc");
    }

    /**
     * 跨域配置
     *
     * @param body 返回的内容体
     */
    @Override
    public Object beforeBodyWrite(Object body, @Nonnull MethodParameter methodParameter, @Nonnull MediaType mediaType, @Nonnull Class<? extends HttpMessageConverter<?>> aClass, @Nonnull ServerHttpRequest serverHttpRequest, @Nonnull ServerHttpResponse serverHttpResponse) {
        /* 防止返回类型不是包装类型，但是抛出异常，被处理成包装类型 */
        if (body instanceof CommonResult) {
            return body;
        }
        Object wrap = CommonResult.success(body,"操作成功");
        /* 防止返回类型为string时需要特殊判断 */
        if (body instanceof String) {
            try {
                return OBJECT_MAPPER.writeValueAsString(wrap);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return wrap;
    }
}
