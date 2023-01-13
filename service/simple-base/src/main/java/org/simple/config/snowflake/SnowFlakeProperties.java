package org.simple.config.snowflake;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 分布式雪花id属性配置
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/9
 */
@Data
@Component
@ConfigurationProperties(prefix = "config.snowflake")
public class SnowFlakeProperties {
    /**
     * 机器码
     */
    private short workerId;
}
