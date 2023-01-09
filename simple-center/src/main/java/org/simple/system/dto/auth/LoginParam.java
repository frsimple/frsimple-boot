package org.simple.system.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * LoginParam
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-11-24 22:16:46
 */
@Data
public class LoginParam {

    @NotNull(message = "请输入验证码")
    private String code;

    @NotNull(message = "网络出错，请稍后重试")
    private String sp;

    @NotNull(message = "请输入用户名")
    private String userName;

    @NotNull(message = "请输入密码")
    private String password;

    private String device;
}
