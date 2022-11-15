package org.simple.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.simple.dto.UserDto;
import org.simple.entity.User;
import org.simple.service.UserService;
import org.simple.utils.CommonResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private RedisTemplate redisTemplate;


    @GetMapping("info")
    @Operation(summary = "查询当前用户信息")
    public CommonResult getUserInfo() {
        return null;
    }


    @GetMapping("menu")
    @Operation(summary = "查询当前用户菜单")
    public CommonResult getUserMenu() {
        return null;
    }

    @GetMapping("list")
    @Operation(summary = "查询用户列表")
    public CommonResult list(Page page, User user) {
        return CommonResult.success(userService.listAll(page, user));
    }

    @GetMapping("list1")
    @Operation(summary = "根据条件查询用户")
    public CommonResult list1(User user) {
        String id = user.getId();
        user.setId(null);
        return CommonResult.success(userService.list(Wrappers.query(user).notIn("id", id)));
    }

    @PostMapping("addUser")
    @Operation(summary = "新增用户信息")
    public CommonResult addUser(@RequestBody UserDto userDto) {
        return null;
    }

    @PostMapping("editUser")
    @Operation(summary = "修改用户信息")
    public CommonResult editUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("delUser/{id}")
    @Operation(summary = "删除用户信息")
    public CommonResult delUser(@PathVariable("id") String id) {
        return null;
    }

    @GetMapping("lock/{id}")
    @Operation(summary = "锁定用户")
    public CommonResult lock(@PathVariable("id") String id) {
        User user = new User();
        user.setId(id);
        user.setStatus("1");
        userService.updateById(user);
        return CommonResult.successNodata("账户已被锁定");
    }

    @GetMapping("unlock/{id}")
    @Operation(summary = "解锁用户")
    public CommonResult unlock(@PathVariable("id") String id) {
        User user = new User();
        user.setId(id);
        user.setStatus("0");
        userService.updateById(user);
        return CommonResult.successNodata("账户已解锁");
    }

    @GetMapping("resetPwd/{id}")
    @Operation(summary = "重置密码")
    public CommonResult resetPwd(@PathVariable("id") String id) {
        return null;
    }

    @PostMapping("updatePwd")
    @Operation(summary = "修改密码")
    public CommonResult updatePwd(@RequestBody UserDto userDto) {
        return null;
    }

    @PostMapping("checkPwd")
    @Operation(summary = "检验密码是否正确")
    public CommonResult checkPwd(@RequestBody UserDto userDto) {
        return null;
    }

    @PostMapping("updateAvatar")
    public CommonResult updateAvatar(@RequestParam("file") MultipartFile file) {
        return null;
    }

    @GetMapping("queryUser")
    public CommonResult queryUser() {
        return null;
    }

    @PostMapping("updateUser")
    public CommonResult updateUser(@RequestBody User user) {
        return null;
    }

    @GetMapping("sendMsg")
    public CommonResult sendMsg(@RequestParam("phone") String phone) {
        return null;
    }

    @GetMapping("updatePhone")
    public CommonResult updatePhone(@RequestParam("password") String password, @RequestParam("code") String code) {
        return null;
    }

    @GetMapping("sendEmail")
    public CommonResult sendEmail(@RequestParam("email") String email) {
        return null;
    }

    @GetMapping("updateEmail")
    public CommonResult updateEmail(@RequestParam("password") String password, @RequestParam("code") String code) {
        return null;
    }
}
