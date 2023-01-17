package org.simple.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import org.simple.dto.UserDto;

/**
 * 当前信息工具类
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/12/6
 */
public class AuthUtils {

    /**
     * 根据token获取当前用户信息
     *
     * @return 当前人信息
     */
    public static UserDto getUser() {
        String token = StpUtil.getTokenValue();
        final JWT jwt = JWTUtil.parseToken(token);
        jwt.getHeader(JWTHeader.TYPE);
        JSONObject jwtPayload = jwt.getPayloads();
        return JSONUtil.toBean(jwtPayload, UserDto.class);
    }

    /**
     * 获取当前登录人id
     *
     * @return 登录人id
     */
    public static String getUserId() {
        return StpUtil.getLoginId().toString();
    }
}
