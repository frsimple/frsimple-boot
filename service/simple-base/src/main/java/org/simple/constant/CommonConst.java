package org.simple.constant;


import lombok.Data;

/**
 * 字符串常量
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/17
 */
@Data
public class CommonConst {

    /**
     * 逗号
     */
    public static final String STRING_COMMA = ",";

    /**
     * jwtToken前缀
     */
    public static final String TOKEN_PREFIX = "bearer";

    /**
     * 基础配置库
     */
    public static final String DB_BASE = "base";

    /**
     * token的Code
     */
    public static final String TOKEN_CODE = "Authorization";

    /**
     * 警号
     */
    public static final String ALARM_SIGNAL = "#";

    /**
     * MINIO
     */
    public static final String MINIO = "MINIO";

    /**
     * 超级管理员
     */
    public static final String SUPER_ADMIN = "1";
}
