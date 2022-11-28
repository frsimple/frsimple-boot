package org.simple.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.crypto.SecureUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.config.auth.AuthProperties;
import org.simple.dto.LoginDto;
import org.simple.dto.LoginParam;
import org.simple.dto.QueryOnlineUserDto;
import org.simple.dto.QueryOnlineUserParam;
import org.simple.exception.CustomException;
import org.simple.service.IAuthService;
import org.simple.utils.CommonResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

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

    @RequestMapping("userLogout")
    @Operation(summary = "管理员踢人下线")
    public CommonResult userLogout(String userId) {
        StpUtil.kickout(userId);
        return CommonResult.success(null);
    }

    /**
     * 查询在线用户
     * @param queryOnlineUserParam 查询在线用户列表数
     * @return
     */
    @PostMapping("list")
    public CommonResult getList(@RequestBody @Valid QueryOnlineUserParam queryOnlineUserParam) {        // 返回
        return CommonResult.success(loginService.getOnlineUser(queryOnlineUserParam));
    }
}
