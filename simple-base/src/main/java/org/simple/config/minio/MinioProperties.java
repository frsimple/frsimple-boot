package org.simple.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * minio属性配置
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-7/9
 */
@Data
@Component
@ConfigurationProperties(prefix = "config.minio")
public class MinioProperties {
    /**
     * 服务端地址
     */
    private String endpoint;
    /**
     * 账号
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 地址
     */
    private String fileHost;
}
