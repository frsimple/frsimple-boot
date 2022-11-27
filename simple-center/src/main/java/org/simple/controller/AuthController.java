package org.simple.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.SecureUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.config.auth.AuthProperties;
import org.simple.dto.LoginDto;
import org.simple.dto.LoginParam;
import org.simple.exception.CustomException;
import org.simple.service.IAuthService;
import org.simple.utils.CommonResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.KeyPair;
import java.util.Base64;

/**
 * 验证码控制器
 *
 * @author xiaozhi
 * @version v1.0
 * @since 2022-11-24 22:15:00
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/auth")
@Tag(name = "auth", description = "登录相关")
public class AuthController {
    private final IAuthService loginService;

    @SaIgnore
    @PostMapping("/doLogin")
    @Operation(summary = "账号密码登录")
    public CommonResult<LoginDto> doLogin(@RequestBody @Valid LoginParam loginParam) throws CustomException {
        return CommonResult.success(loginService.loginByUserName(loginParam));
    }

    @RequestMapping("logout")
    @Operation(summary = "注销登录")
    public CommonResult logout() {
        StpUtil.logout();
        return CommonResult.success(null);
    }
}
