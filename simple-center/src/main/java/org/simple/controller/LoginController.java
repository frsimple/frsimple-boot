package org.simple.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import org.simple.dto.LoginDto;
import org.simple.dto.LoginParam;
import org.simple.entity.User;
import org.simple.service.ILoginService;
import org.simple.service.IUserService;
import org.simple.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 验证码控制器
 *
 * @author xiaozhi
 * @version v1.0
 * @since 2022-11-24 22:15:00
 */
@RestController
@RequestMapping("/center/auth")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @Autowired
    private IUserService userService;


    @PostMapping("/byUserName")
    @SaIgnore
    public CommonResult<LoginDto> authByUserName(@RequestBody @Valid LoginParam loginParam) {
        // 直接调用service方法
        return CommonResult.success(loginService.loginByUserName(loginParam));
    }


    @GetMapping("/getUserInfo")
    @SaIgnore
    public CommonResult<User> getCurrentUserInfo() {
        String userId = loginService.getCurrentUserId();
        User user = userService.getById(userId);
        // 直接调用service方法
        return CommonResult.success(user);
    }
}
