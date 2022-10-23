package org.simple.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT属性配置
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/8
 */
@Data
@Component
@ConfigurationProperties(prefix = "config.jwt")
public class JwtProperties {
    /**
     * token参数名称
     */
    private String tokenCode;
    /**
     * 密钥
     */
    private String secretKey;
    /**
     * 过期时间
     */
    private Integer expTime;

}
