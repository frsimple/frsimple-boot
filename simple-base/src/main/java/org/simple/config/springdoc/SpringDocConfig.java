package org.simple.config.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerConfig
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/7
 */
@Configuration
@RequiredArgsConstructor
public class SpringDocConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";
    private final SpringDocProperties springDocProperties;

    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder()
                .group("system").displayName("系统管理")
                .pathsToMatch("/api/center/system/**")
                .build();
    }

    @Bean
    public GroupedOpenApi onlineApi() {
        return GroupedOpenApi.builder()
                .group("online").displayName("在线开发")
                .pathsToMatch("/api/center/online/**")
                .build();
    }


    /**
     * 主要是这个方法，其他的方法是抽出去的
     * 在 basePackage 里面写需要生成文档的 controller 路径
     *
     * @return openApi信息
     */
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    /**
     * spring-doc文档的描述
     *
     * @return 文档描述
     */
    private Info apiInfo() {
        return new Info()
                .title(springDocProperties.getTitle())
                .description(springDocProperties.getDescription())
                .contact(contactInfo())
                .version(springDocProperties.getVersion())
                .license(license());
    }

    /**
     * spring-doc文档的描述
     *
     * @return 作者信息
     */
    private Contact contactInfo() {
        return new Contact()
                .email(springDocProperties.getEmail())
                .name(springDocProperties.getName())
                .url(springDocProperties.getUrl());
    }

    /**
     * spring-doc文档的描述
     *
     * @return 授权信息
     */
    private License license() {
        return new License()
                .name("Apache-2.0")
                .url("https://opensource.org/licenses/Apache-2.0");
    }
}
