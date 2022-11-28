package org.simple.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.constant.RedisConstant;
import org.simple.dto.UserDto;
import org.simple.entity.User;
import org.simple.service.IAuthService;
import org.simple.service.IUserService;
import org.simple.sms.EmailUtil;
import org.simple.sms.SmsUtil;
import org.simple.storage.OssUtil;
import org.simple.utils.ComUtil;
import org.simple.utils.CommonResult;
import org.simple.utils.RandomUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/user")
@Tag(name = "user", description = "用户管理")
public class UserController {
    private final IUserService userService;
    private final IAuthService authService;
    private final RedisTemplate redisTemplate;

    @GetMapping("info")
    @Operation(summary = "查询当前用户信息")
    public CommonResult getUserInfo() {
        String userId = authService.getCurrentUserId();
        User user = userService.getById(userId);
        Map<String, Object> userInfo =
                BeanUtil.beanToMap(userId);
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("nickname", user.getNickname());
        Map<String, Object> result = new HashMap<>(10);
        result.put("roles", StpUtil.getPermissionList(userId));
        result.put("user", userInfo);
        return CommonResult.success(result);
    }

    @GetMapping("/menu")
    @Operation(summary = "查询当前用户菜单")
    public CommonResult getUserMenu() {
        return CommonResult.success(userService.getUserMenu(authService.getCurrentUserId()));
    }

    @GetMapping("list")
    @Operation(summary = "查询用户列表")
    @SaCheckPermission(value = {"system:user:query"}, mode = SaMode.OR)
    public CommonResult list(Page page, User user) {
        return CommonResult.success(userService.listAll(page, user));
    }

    @GetMapping("list1")
    @Operation(summary = "根据条件查询用户")
    public List<User> list1(User user) {
        String id = user.getId();
        user.setId(null);
        return userService.list(Wrappers.query(user).notIn("id", id));
    }

    @PostMapping("addUser")
    @Operation(summary = "新增用户信息")
    @SaCheckPermission(value = {"system:user:add"}, mode = SaMode.OR)
    public CommonResult addUser(@RequestBody UserDto userDto) {
        String userid = RandomUtil.getUserId();
        User user = new User();
        user.setId(userid);
        user.setStatus("0");
        user.setErrorcount(0);
        user.setCreatedate(LocalDateTime.now());
        user.setUpdatedate(LocalDateTime.now());
        String md5Pwd = DigestUtil.md5Hex(userDto.getUsername().toLowerCase() + userDto.getPassword());
        user.setPassword(md5Pwd);
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setNickname(userDto.getNickname());
        user.setUsername(userDto.getUsername());
        user.setTenant(userDto.getTenant());
        user.setOrgan(userDto.getOrgan());
        userService.save(user);
        userService.insertUserTenant(RandomUtil.getUserTenantId(), user.getTenant(), userid);
        //for循环插入用户角色关联关系数据
        String[] roles = userDto.getRoles().split(",");
        for (String role : roles) {
            userService.insertRoleUser(RandomUtil.getRoleUserId(), role, userid);
        }
        return CommonResult.successNodata("新增成功");
    }

    @PostMapping("editUser")
    @Operation(summary = "修改用户信息")
    @SaCheckPermission(value = {"system:user:edit"}, mode = SaMode.OR)
    public CommonResult editUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("delUser/{id}")
    @Operation(summary = "删除用户信息")
    @SaCheckPermission(value = {"system:user:del"}, mode = SaMode.OR)
    public CommonResult delUser(@PathVariable("id") String id) {
        if ("1".equals(id)) {
            return CommonResult.failed("不允许删除超级管理员");
        }
        return userService.delUser(id);
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
        User user = userService.getById(id);
        String md5Pwd = DigestUtil.md5Hex(user.getUsername().toLowerCase() + "888888");
        user.setPassword(md5Pwd);
        userService.updateById(user);
        return CommonResult.successNodata("账户密码已重置");
    }

    @PostMapping("updatePwd")
    @Operation(summary = "修改密码")
    public CommonResult updatePwd(@RequestBody UserDto userDto) {
        User user = new User();
        if (StrUtil.isEmpty(userDto.getId())) {
            userDto.setId(authService.getCurrentUserId());
        }
        user.setId(userDto.getId());
        String md5Pwd = DigestUtil.md5Hex(user.getUsername().toLowerCase() + userDto.getNPassword());
        user.setPassword(md5Pwd);
        userService.updateById(user);
        return CommonResult.successNodata("账户密码修改成功");
    }

    @PostMapping("checkPwd")
    @Operation(summary = "检验密码是否正确")
    public CommonResult checkPwd(@RequestBody UserDto userDto) {
        if (StrUtil.isEmpty(userDto.getId())) {
            userDto.setId(authService.getCurrentUserId());
        }
        User user = userService.getById(userDto.getId());
        String md5Pwd = DigestUtil.md5Hex(userDto.getUsername().toLowerCase() + userDto.getNPassword());
        if (!user.getPassword().equals(md5Pwd)) {
            return CommonResult.failed("密码错误");
        }
        return CommonResult.successNodata("密码正确");
    }

    @PostMapping("updateAvatar")
    @Operation(summary = "更新用户头像")
    public CommonResult updateAvatar(@RequestParam("file") MultipartFile file) {
        File file1 = ComUtil.multipartToFile(file);
        //上传图片
        CommonResult result = OssUtil.getAliOss(redisTemplate).fileUpload(file1, false, authService.getCurrentUserId());
        if (result.getCode() == 0) {
            //上传成功，头像用户头像
            User user = new User();
            user.setAvatar(result.getData().toString());
            user.setId(authService.getCurrentUserId());
            userService.updateById(user);
            return CommonResult.successNodata(result.getData().toString());
        } else {
            return result;
        }
    }

    @GetMapping("queryUser")
    @Operation(summary = "查询当前用户")
    public CommonResult queryUser() {
        User user = userService.getById(authService.getCurrentUserId());
        User r = new User();
        r.setId(user.getId());
        r.setEmail(StrUtil.hide(user.getEmail(), 3, 5));
        r.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
        r.setNickname(user.getNickname());
        return CommonResult.success(r);
    }

    @PostMapping("updateUser")
    @Operation(summary = "更新用户信息")
    public CommonResult updateUser(@RequestBody User user) {
        if (!user.getId().equals(authService.getCurrentUserId())) {
            return CommonResult.failed("非法操作");
        }
        return CommonResult.success(userService.updateById(user));
    }

    @GetMapping("sendMsg")
    @Operation(summary = "发送消息")
    public CommonResult sendMsg(@RequestParam("phone") String phone) {
        //判断是否和当前手机号一直
        User phoneUser = new User();
        phoneUser.setPhone(phone);
        if (userService.list(Wrappers.query(phoneUser)).size() != 0) {
            return CommonResult.failed("新手机号已注册，不能重复使用");
        }

        String key = RedisConstant.PHONE_UPDATE_CODE_STR + authService.getCurrentUserId();
        //校验是否发送过短信以免重复发送60秒只能发送一次
        if (redisTemplate.hasKey(key)) {
            Long seconds = redisTemplate.opsForValue().getOperations().getExpire(key);
            if (seconds > 0) {
                return CommonResult.failed("已获取过短信，请等待" + seconds + "秒之后在获取");
            }
        }
        //获取四位随机数字
        String rom = cn.hutool.core.util.RandomUtil.randomNumbers(4);
        try {
            CommonResult result = SmsUtil.getTencentSms(redisTemplate).sendSms(null, "865723",
                    new String[]{rom}, new String[]{phone});
            if (result.getCode() == 1) {
                return CommonResult.failed("发送失败：" + result.getMsg());
            }
        } catch (Exception e) {
            return CommonResult.failed("发送失败：" + e.getMessage());
        }
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(RedisConstant.PHONE_UPDATE_CODE_STR + authService.getCurrentUserId(),
                rom + "_" + phone, RedisConstant.CODE_TIMEOUT, TimeUnit.SECONDS);
        return CommonResult.successNodata("发送成功");
    }

    @GetMapping("updatePhone")
    @Operation(summary = "更新手机号")
    public CommonResult updatePhone(@RequestParam("password") String password,
                                    @RequestParam("code") String code) {
        String key = RedisConstant.PHONE_UPDATE_CODE_STR + authService.getCurrentUserId();
        Object smsStr = redisTemplate.opsForValue().get(key);
        if (null == smsStr) {
            return CommonResult.failed("验证码错误");
        }
        String smsCode = String.valueOf(smsStr).split("_")[0];
        String phone = String.valueOf(smsStr).split("_")[1];
        if (!code.equals(smsCode)) {
            return CommonResult.failed("验证码错误");
        }
        User userInfo = userService.getById(authService.getCurrentUserId());
        String md5Pwd = DigestUtil.md5Hex(userInfo.getUsername().toLowerCase() + password);
        if (!userInfo.getPassword().equals(md5Pwd)) {
            return CommonResult.failed("用户密码错误");
        }
        User user = new User();
        user.setId(authService.getCurrentUserId());
        user.setPhone(phone);
        userService.updateById(user);

        return CommonResult.successNodata("关联手机绑定成功");
    }


    @GetMapping("sendEmail")
    @Operation(summary = "发送邮件")
    public CommonResult sendEmail(@RequestParam("email") String email) {
        //判断是否和当前手机号一直
        User emailUser = new User();
        emailUser.setEmail(email);
        if (userService.list(Wrappers.query(emailUser)).size() != 0) {
            return CommonResult.failed("新邮箱已注册，不能重复使用");
        }

        String key = RedisConstant.EMAIL_UPDATE_CODE_STR + authService.getCurrentUserId();
        //校验是否发送过短信以免重复发送60秒只能发送一次
        if (redisTemplate.hasKey(key)) {
            Long seconds = redisTemplate.opsForValue().getOperations().getExpire(key);
            if (seconds > 0) {
                return CommonResult.failed("已获取过验证码，请等待" + seconds + "秒之后在获取");
            }
        }
        //获取四位随机数字
        String rom = cn.hutool.core.util.RandomUtil.randomNumbers(4);
        try {
            CommonResult result = EmailUtil.getInstance(redisTemplate)
                    .sendEmail("更换绑定邮箱地址", "验证码：" + rom,
                            new String[]{email}, true, null);
            if (result.getCode() == 1) {
                return CommonResult.failed("发送失败：" + result.getMsg());
            }
        } catch (Exception e) {
            return CommonResult.failed("发送失败：" + e.getMessage());
        }
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(RedisConstant.EMAIL_UPDATE_CODE_STR + authService.getCurrentUserId(), rom + "_" + email, RedisConstant.CODE_TIMEOUT, TimeUnit.SECONDS);
        return CommonResult.successNodata("发送成功");
    }


    @GetMapping("updateEmail")
    @Operation(summary = "更新邮箱")
    public CommonResult updateEmail(@RequestParam("password") String password,
                                    @RequestParam("code") String code) {
        String key = RedisConstant.EMAIL_UPDATE_CODE_STR + authService.getCurrentUserId();
        Object smsStr = redisTemplate.opsForValue().get(key);
        if (null == smsStr) {
            return CommonResult.failed("验证码错误");
        }
        String smsCode = String.valueOf(smsStr).split("_")[0];
        String email = String.valueOf(smsStr).split("_")[1];
        if (!code.equals(smsCode)) {
            return CommonResult.failed("验证码错误");
        }
        User userInfo = userService.getById(authService.getCurrentUserId());
        String md5Pwd = DigestUtil.md5Hex(userInfo.getUsername().toLowerCase() + password);
        if (!userInfo.getPassword().equals(md5Pwd)) {
            return CommonResult.failed("用户密码错误");
        }
        //验证成功开始更新数据
        User user = new User();
        user.setId(authService.getCurrentUserId());
        user.setEmail(email);
        userService.updateById(user);
        return CommonResult.successNodata("更新绑定邮箱成功");
    }
}
