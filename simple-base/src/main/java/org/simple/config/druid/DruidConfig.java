package org.simple.config.druid;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * DruidConfig
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/9
 */
@Configuration
@RequiredArgsConstructor
public class DruidConfig {

    private final DruidProperties druidProperties;

    /**
     * 配置 Druid 监控管理后台的Servlet
     *
     * @return 配置druid
     */
    @Bean
    public ServletRegistrationBean<Servlet> statViewServlet() {
        StatViewServlet statViewServlet = new StatViewServlet();
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(statViewServlet, "/druid/*");
        // 这些参数可以在 com.alibaba.druid.support.http.StatViewServlet 的父类 com.alibaba.druid.support.http.ResourceServlet 中找到
        Map<String, String> initParams = new HashMap<>(10);
        initParams.put("loginUsername", druidProperties.getLoginUsername());
        initParams.put("loginPassword", druidProperties.getLoginPassword());
        //默认就是允许所有访问
        initParams.put("allow", druidProperties.getAllow());
        //deny：Druid 后台拒绝谁访问，表示禁止此ip访问
        if (StrUtil.isBlankIfStr(druidProperties.getDeny())) {
            initParams.put("deny", druidProperties.getDeny());
        }
        bean.setInitParameters(initParams);
        return bean;
    }

    /**
     * 配置一个web监控的filter
     *
     * @return druid过滤
     */
    @Bean
    public FilterRegistrationBean<Filter> webStatFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>(10);
        initParams.put("exclusions", druidProperties.getExclusions());
        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Collections.singletonList("/*"));
        return bean;
    }
}
