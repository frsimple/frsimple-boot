package org.simple.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.simple.dto.UserDto;
import org.springframework.stereotype.Component;

/**
 * 当前信息工具类
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/12/6
 */
@Component
@RequiredArgsConstructor
public class CurrentUtil {

    /**
     * 根据token获取当前用户信息
     *
     * @return 当前人信息
     */
    public UserDto getUserByToken() {
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
    public String getCurrentUserId() {
        return StpUtil.getLoginId().toString();
    }
}
