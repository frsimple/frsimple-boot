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

}
