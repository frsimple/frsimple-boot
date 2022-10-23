package org.simple.config.springdoc;//package com.admin.config.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Swagger属性配置
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/9
 */
@Data
@Component
@ConfigurationProperties(prefix = "config.springdoc")
public class SpringDocProperties {
    /**
     * 标题
     */
    private String title;

    /**
     * 说明
     */
    private String description;

    /**
     * 版本
     */
    private String version;

    /**
     * 作者名称
     */
    private String name;

    /**
     * 作者地址
     */
    private String url;

    /**
     * 作者邮箱
     */
    private String email;
}
