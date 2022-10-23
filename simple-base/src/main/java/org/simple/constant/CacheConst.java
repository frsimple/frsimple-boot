package org.simple.constant;

import lombok.Data;

/**
 * 缓存常量Key
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/16
 */
@Data
public class CacheConst {

    /**
     * 验证码缓存
     */
    public static final String CACHE_V_CODE = "vCode_";

    /**
     * 菜单缓存
     */
    public static final String CACHE_MENU = "menu_";

    /**
     * 权限缓存
     */
    public static final String CACHE_PERMISSION = "permission_";

    /**
     * 角色缓存
     */
    public static final String CACHE_ROLE = "role_";

    /**
     * token缓存
     */
    public static final String CACHE_TOKEN = "loginToken_";

    /**
     * 用户信息缓存
     */
    public static final String CACHE_USERINFO = "loginUserInfo_";

}
