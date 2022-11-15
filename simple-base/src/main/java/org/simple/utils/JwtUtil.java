package org.simple.utils;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/16
 */
public class JwtUtil {

    /**
     * 获取实际的token
     *
     * @param token jwtToken
     * @return 分隔后的token
     */
    public static String getRealToken(String token) {
        return token.split(" ")[1];
    }
}
