package org.simple.config.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * web过滤器
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/11
 */
@Configuration
@RequiredArgsConstructor
public class WebFilterConfig implements WebMvcConfigurer {

    private final WebInterceptorHandler webInterceptorHandler;

    /**
     * 跨域配置
     *
     * @param registry 配置属性
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 自定拦截器
     *
     * @param registry 配置属性
     */
    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(webInterceptorHandler).addPathPatterns("/**").excludePathPatterns(excludePath());
    }

    /**
     * 排除拦截的url
     */
    private List<String> excludePath() {
        List<String> list = new ArrayList<>();
        list.add("/api/base/basics/auth/login");
        list.add("/api/base/basics/auth/getPublicKey");
        list.add("/swagger-ui/index.html");
        list.add("/swagger-resources/**");
        list.add("/favicon.ico");
        list.add("/v3/api-docs/**");
        list.add("/swagger-ui/**");
        list.add("/error/**");
        list.add("/shop/wechat/**/api/**");
        return list;
    }
}
