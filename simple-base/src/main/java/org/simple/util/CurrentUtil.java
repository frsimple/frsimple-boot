package org.simple.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import org.simple.config.jwt.JwtProperties;
import org.simple.constant.CacheConst;
import org.simple.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 当前信息工具类
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/16
 */
@Component
@RequiredArgsConstructor
public class CurrentUtil {

    private final JwtProperties jwtProperties;
    private final RedisUtil redisUtil;

    /**
     * 从request上下文获取token信息
     *
     * @return token
     */
    public String getTokenByRequest() {
        return ServletUtil.getHeader(jwtProperties.getTokenCode());
    }

    /**
     * 从redis获取token
     *
     * @param userId 用户id
     * @return token
     */
    public String getToken(Long userId) {
        return redisUtil.get(CacheConst.CACHE_TOKEN + userId).toString();
    }

    /**
     * 添加token信息到redis
     *
     * @param token   jwtToken
     * @param userId  用户id
     * @param expTime 过期时间
     */
    public void setToken(String token, Long userId, Long expTime) {
        String tokenKey = CacheConst.CACHE_TOKEN + userId;
        //记录在线token信息
        redisUtil.set(tokenKey, token, expTime);
    }

    /**
     * 移除token缓存
     *
     * @param userId 用户id
     */
    public void removeToken(String userId) {
        redisUtil.delete(CacheConst.CACHE_TOKEN + userId);
    }

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    public UserInfo getUserInfo() {
        String token = getTokenByRequest();
        return this.getUserInfo(token);
    }

    /**
     * 移除userinfo信息
     *
     * @param userId 用户id
     */
    public void setUserInfo(String userId) {
        redisUtil.delete(CacheConst.CACHE_USERINFO + userId);
    }

    /**
     * 获取当前用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    public UserInfo getUserInfo(Long userId) {
        String token = getToken(userId);
        return this.getUserInfo(token);
    }

    /**
     * 根据token从redis获取用户信息
     *
     * @param token jwtToken
     * @return 用户信息
     */
    public UserInfo getUserInfo(String token) {
        UserInfo userInfo = new UserInfo();
        String tokens;
        if (token != null) {
            token = JwtUtil.getRealToken(token);
            tokens = token;
        } else {
            tokens = this.getTokenByRequest();
            if (tokens == null) {
                return new UserInfo();
            }
            tokens = JwtUtil.getRealToken(tokens);
        }
        if (tokens != null) {
            String userId = JWTUtil.parseToken(token).getPayload("userId").toString();
            String loginUserInfoKey = CacheConst.CACHE_USERINFO + userId;
            userInfo = JSONUtil.toBean(String.valueOf(redisUtil.get(loginUserInfoKey)), UserInfo.class);
        }
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    /**
     * 添加当前用户信息到redis
     *
     * @param userInfo 用户信息
     * @param expTime  过期时间
     */
    public void setUserInfo(UserInfo userInfo, Long expTime) {
        String loginUserInfoKey = CacheConst.CACHE_USERINFO + userInfo.getUserId();
        //记录userInfo信息
        redisUtil.set(loginUserInfoKey, JSONUtil.toJsonStr(userInfo), expTime);
    }

    /**
     * 从redis移除当前用户信息
     *
     * @param userId 用户id
     */
    public void removeUserInfo(String userId) {
        redisUtil.delete(CacheConst.CACHE_USERINFO + userId);
    }

    /**
     * 添加所有信息到redis
     *
     * @param userInfo 用户信息
     */
    public void setCurrent(UserInfo userInfo) {
        DateTime startTime = DateTime.now();
        DateTime endTime = DateUtil.offset(startTime, DateField.MINUTE, jwtProperties.getExpTime());
        long expTime = DateUtil.betweenMs(startTime, endTime);
        //记录userInfo信息
        this.setUserInfo(userInfo, expTime);
        //记录token信息
        this.setToken(userInfo.getToken(), userInfo.getUserId(), expTime);
    }

    /**
     * 移除当前登陆人的token
     */
    public void removeCurrent() {
        UserInfo userInfo = this.getUserInfo();
        redisUtil.delete(CacheConst.CACHE_TOKEN + userInfo.getUserId());
        redisUtil.delete(CacheConst.CACHE_USERINFO + userInfo.getUserId());
    }

    /**
     * 获取菜单树缓存
     *
     * @param userId 用户id
     * @return 菜单列表
     */
    public String getUserMenuTree(Long userId) {
        return (String) redisUtil.get(CacheConst.CACHE_MENU + userId);

    }

    /**
     * 设置菜单树缓存
     *
     * @param userId 用户id
     * @param data   菜单信息的jsonString
     */
    public void setUserMenuTree(Long userId, String data) {
        redisUtil.set(CacheConst.CACHE_MENU + userId, data);
    }

    /**
     * 移除用户菜单缓存
     *
     * @param userId 用户id
     */
    public void removeUserMenuTree(Long userId) {
        redisUtil.delete(CacheConst.CACHE_MENU + userId);
    }

    /**
     * 获取用户资源缓存
     *
     * @param userId 用户id
     * @return 资源列表
     */
    public String getUserPermission(Long userId) {
        return (String) redisUtil.get(CacheConst.CACHE_PERMISSION + userId);
    }

    /**
     * 设置用户资源缓存
     *
     * @param userId 用户id
     * @param data   资源list的jsonString
     */
    public void setUserPermission(Long userId, String data) {
        redisUtil.set(CacheConst.CACHE_PERMISSION + userId, data);
    }

    /**
     * 移除用户资源缓存
     *
     * @param userId 用户id
     */
    public void removeUserPermission(Long userId) {
        redisUtil.delete(CacheConst.CACHE_PERMISSION + userId);
    }

    /**
     * 获取用户角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    public String getUserRole(Long userId) {
        return (String) redisUtil.get(CacheConst.CACHE_ROLE + userId);
    }

    /**
     * 设置用户角色缓存
     *
     * @param userId 用户id
     * @param data   角色信息de jsonString
     */
    public void setUserRole(Long userId, String data) {
        redisUtil.set(CacheConst.CACHE_ROLE + userId, data);
    }

    /**
     * 移除用户角色缓存
     *
     * @param userId 用户id
     */
    public void removeUserRole(Long userId) {
        redisUtil.delete(CacheConst.CACHE_ROLE + userId);
    }

    /**
     * 清除当前用户权限
     */
    public void clearUserAuth() {
        UserInfo userInfo = this.getUserInfo();
        this.removeUserMenuTree(userInfo.getUserId());
        this.removeUserPermission(userInfo.getUserId());
        this.removeUserRole(userInfo.getUserId());
    }

    /**
     * 清除当前用户权限
     *
     * @param userId 用户id
     */
    public void clearUserAuthByUserId(Long userId) {
        this.removeUserMenuTree(userId);
        this.removeUserPermission(userId);
        this.removeUserRole(userId);
    }
}
