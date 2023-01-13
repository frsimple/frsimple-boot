package org.simple.utils;

import org.simple.constant.CommonConst;

import javax.servlet.http.HttpServletRequest;

/**
 * ip地址工具类
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-7-17
 */
public class IpUtil {

    /**
     * ip空值
     */
    public static final String UNKNOWN = "unknown";
    /**
     * ip长度
     */
    public static final int IP_LENGTH = 15;
    /**
     * 本地ipv6
     */
    public static final String STRING_IP = "0:0:0:0:0:0:0:1";
    /**
     * 本地ip
     */
    public static final String LOCAL_IP = "127.0.0.1";

    /**
     * 获取登录ip地址
     *
     * @return ip地址
     */
    public static String getIpAddress() {
        HttpServletRequest request = ServletUtil.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-real-ip");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.length() > IP_LENGTH) {
            if (ip.indexOf(CommonConst.STRING_COMMA) > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        if (STRING_IP.equals(ip)) {
            ip = LOCAL_IP;
        }
        return ip;
    }
}
