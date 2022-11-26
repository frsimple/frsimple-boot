package org.simple.config.satoken;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import com.github.yitter.idgen.YitIdHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * sa-token配置
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-7-17
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * 重写 Sa-Token 框架内部算法策略
     */
    @Autowired
    public void rewriteSaStrategy() {
        // 重写 Token 生成策略 
        SaStrategy.me.createToken = (loginId, loginType) -> {
            // 生成雪花Id
            return String.valueOf(YitIdHelper.nextId());
        };
    }

    /**
     * 注册拦截器
     *
     * @param registry 仓储
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 的路由拦截器
        registry.addInterceptor(new SaInterceptor(handle -> SaRouter
                        .match("/**")
                        .notMatch(excludePaths())))
                .addPathPatterns("/**");
    }

    /**
     * 动态获取哪些path可以忽略鉴权
     */
    public List<String> excludePaths() {
        // 此处仅为示例，实际项目你可以写任意代码来查询这些path,也可根据@SaIgnore忽略请求
        return ListUtil.toList();
    }
}
