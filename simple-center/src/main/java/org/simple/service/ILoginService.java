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
public interface ILoginService {

    /**
     * 通过用户名登录
     * @param loginParam 登录参数
     * @return 返回登录实体信息
     */
    LoginDto loginByUserName(LoginParam loginParam);
}
