package org.simple.service;

import org.simple.dto.LoginDto;
import org.simple.dto.LoginParam;

/**
 * LoginService
 *
 * @author xiaozhi
 * @version v1.0
 * @since 2022-11-24 22:24:29
 */
public interface IAuthService {

    /**
     * 通过用户名登录
     * @param loginParam 登录参数
     * @return 返回登录实体信息
     */
    LoginDto loginByUserName(LoginParam loginParam);


    /**
     * 获取当前登录的用户ID
     * @return
     */
     String getCurrentUserId();


    /**
     * 获取当前用户的token
     * @return
     */
     String getCurrentToken();
}
