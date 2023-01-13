package org.simple.config.druid;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * druid属性配置
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/9
 */
@Data
@Component
@ConfigurationProperties(prefix = "config.druid")
public class DruidProperties {
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 登录名称
     */
    private String loginUsername;
    /**
     * 登录密码
     */
    private String loginPassword;
    /**
     * 允许访问的ip
     */
    private String allow;
    /**
     * 不允许访问的ip
     */
    private String deny;
    /**
     * 排除的请求类型
     */
    private String exclusions;
}
