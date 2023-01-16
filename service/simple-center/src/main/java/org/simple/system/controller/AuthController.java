package org.simple.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.dto.PageResult;
import org.simple.system.dto.auth.LoginParam;
import org.simple.system.dto.onlineuser.OnlineUserDto;
import org.simple.system.dto.onlineuser.OnlineUserQuery;
import org.simple.exception.CustomException;
import org.simple.system.service.IAuthService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 验证码控制器
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-11-24 22:15:00
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/system/auth")
@Tag(name = "auth", description = "登录相关")
public class AuthController {
    private final IAuthService authService;

    @SaIgnore
    @PostMapping("/doLogin")
    @Operation(summary = "账号密码登录")
    public String doLogin(@RequestBody @Valid LoginParam loginParam) throws CustomException {
        return authService.loginByUserName(loginParam);
    }

    @GetMapping("logout")
    @Operation(summary = "注销登录")
    public void logout() {
        StpUtil.logout(StpUtil.getLoginId(), StpUtil.getLoginDevice());
    }

    @GetMapping("userLogout")
    @Operation(summary = "管理员踢人下线")
    public void userLogout(String userId) {
        StpUtil.kickout(userId);
    }

    @GetMapping("list")
    @Operation(summary = "查询在线用户")
    public PageResult<OnlineUserDto> getList(OnlineUserQuery query) {
        List<OnlineUserDto> list = authService.getOnlineUser(query);
        PageResult<OnlineUserDto> pageResult = new PageResult<>(list);
        pageResult.setTotal(query.getTotal());
        return pageResult;
    }

    @SaIgnore
    @GetMapping("/getCode")
    @Operation(summary = "获取验证码")
    public void getCode(HttpServletResponse response, @RequestParam("sp") String sp) throws IOException {
        authService.getCode(response, sp);
    }
}
