package org.simple.config.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 认证相关配置
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/23
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {
    /**
     * 密码锁定时间
     */
    private Integer lockTime;
    /**
     * 密码错误次数
     */
    private Integer errorNumber;
    /**
     * 对称加密key
     */
    private String publicKey;
    /**
     * 对称加密key
     */
    private String privateKey;

}
