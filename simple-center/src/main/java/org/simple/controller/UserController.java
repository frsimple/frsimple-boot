package org.simple.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.simple.constant.RedisConstant;
import org.simple.dto.UserDto;
import org.simple.entity.User;
import org.simple.service.UserService;
import org.simple.sms.EmailUtil;
import org.simple.storage.OssUtil;
import org.simple.utils.ComUtil;
import org.simple.utils.CommonResult;
import org.simple.utils.RedomUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private RedisTemplate redisTemplate;


    @GetMapping("info")
    @Operation(summary = "查询当前用户信息")
    public CommonResult getUserInfo() {
        User user = userService.getById(AuthUtils.getUser().getId());
        Map<String, Object> userInfo =
                BeanUtil.beanToMap(AuthUtils.getUser());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("nickname", user.getNickname());
        Map<String, Object> result = new HashMap<>();
        result.put("roles", AuthUtils.getAuthoritys());
        result.put("user", userInfo);
        return CommonResult.success(result);
    }


    @GetMapping("menu")
    @Operation(summary = "查询当前用户菜单")
    public CommonResult getUserMenu() {
        return CommonResult.success(userService.getUserMenu(AuthUtils.getUser().getId()));
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
        String userid = RedomUtil.getUserId();
        User user = new User();
        user.setId(userid);
        user.setStatus("0");
        user.setError(0);
        user.setCreatedate(LocalDateTime.now());
        user.setUpdatedate(LocalDateTime.now());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setNickname(userDto.getNickname());
        user.setUsername(userDto.getUsername());
        user.setTenant(userDto.getTenant());
        user.setOrgan(userDto.getOrgan());
        userService.save(user);
        userService.insertUserTenant(RedomUtil.getUserTenantId(), user.getTenant(), userid);
        //for循环插入用户角色关联关系数据
        String[] roles = userDto.getRoles().split(",");
        for (String role : roles) {
            userService.insertRoleUser(RedomUtil.getRoleUserId(), role, userid);
        }
        return CommonResult.successNodata("新增成功");
    }

    @PostMapping("editUser")
    @Operation(summary = "修改用户信息")
    public CommonResult editUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("delUser/{id}")
    @Operation(summary = "删除用户信息")
    public CommonResult delUser(@PathVariable("id") String id) {
        if (id.equals("1")) {
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
        User user = new User();
        user.setId(id);
        user.setPassword(passwordEncoder.encode("888888"));
        userService.updateById(user);
        return CommonResult.successNodata("账户密码已重置");
    }

    @PostMapping("updatePwd")
    @Operation(summary = "修改密码")
    public CommonResult updatePwd(@RequestBody UserDto userDto) {
        User user = new User();
        if (StringUtils.isEmpty(userDto.getId())) {
            userDto.setId(AuthUtils.getUser().getId());
        }
        user.setId(userDto.getId());
        user.setPassword(passwordEncoder.encode(userDto.getNPassword()));
        userService.updateById(user);
        return CommonResult.successNodata("账户密码修改成功");
    }

    @PostMapping("checkPwd")
    @Operation(summary = "检验密码是否正确")
    public CommonResult checkPwd(@RequestBody UserDto userDto) {
        if (StrUtil.isEmpty(userDto.getId())) {
            userDto.setId(AuthUtils.getUser().getId());
        }
        User user = userService.getById(userDto.getId());
        if (!passwordEncoder.matches(userDto.getPassword(),
                user.getPassword())) {
            return CommonResult.failed("密码错误");
        }
        return CommonResult.successNodata("密码正确");
    }

    @PostMapping("updateAvatar")
    public CommonResult updateAvatar(@RequestParam("file") MultipartFile file) {
        File file1 = ComUtil.MultipartToFile(file);
        //上传图片
        CommonResult result =
                OssUtil.getAliOss(redisTemplate).fileUpload(file1, false, AuthUtils.getUser().getId());
        if (result.getCode() == 0) {
            //上传成功，头像用户头像
            User user = new User();
            user.setAvatar(result.getData().toString());
            user.setId(AuthUtils.getUser().getId());
            userService.updateById(user);
            return CommonResult.successNodata(result.getData().toString());
        } else {
            return result;
        }
    }

    @GetMapping("queryUser")
    public CommonResult queryUser() {
        User user = userService.getById(AuthUtils.getUser().getId());
        User r = new User();
        r.setId(user.getId());
        r.setEmail(StrUtil.hide(user.getEmail(), 3, 5));
        r.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
        r.setNickname(user.getNickname());
        return CommonResult.success(r);
    }

    @PostMapping("updateUser")
    public CommonResult updateUser(@RequestBody User user) {
        if (!user.getId().equals(AuthUtils.getUser().getId())) {
            return CommonResult.failed("非法操作");
        }
        return CommonResult.success(userService.updateById(user));
    }


    @GetMapping("sendMsg")
    public CommonResult sendMsg(@RequestParam("phone") String phone) {
        //判断是否和当前手机号一直
        User phoneUser = new User();
        phoneUser.setPhone(phone);
        if (userService.list(Wrappers.query(phoneUser)).size() != 0) {
            return CommonResult.failed("新手机号已注册，不能重复使用");
        }

        String key = RedisConstant.PHONE_UPDATE_CODE_STR + AuthUtils.getUser().getId();
        //校验是否发送过短信以免重复发送60秒只能发送一次
        if (redisTemplate.hasKey(key)) {
            Long seconds = redisTemplate.opsForValue().getOperations().getExpire(key);
            if (seconds > 0) {
                return CommonResult.failed("已获取过短信，请等待" + seconds + "秒之后在获取");
            }
        }
        //获取四位随机数字
        String rom = RandomUtil.randomNumbers(4);
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
        redisTemplate.opsForValue().set(RedisConstant.PHONE_UPDATE_CODE_STR + AuthUtils.getUser().getId(),
                rom + "_" + phone, RedisConstant.CODE_TIMEOUT, TimeUnit.SECONDS);
        return CommonResult.successNodata("发送成功");
    }


    @GetMapping("updatePhone")
    public CommonResult updatePhone(@RequestParam("password") String password,
                                    @RequestParam("code") String code) {
        String key = RedisConstant.PHONE_UPDATE_CODE_STR + AuthUtils.getUser().getId();
        Object smsStr = redisTemplate.opsForValue().get(key);
        if (null == smsStr) {
            return CommonResult.failed("验证码错误");
        }
        String smsCode = String.valueOf(smsStr).split("_")[0];
        String phone = String.valueOf(smsStr).split("_")[1];
        if (!code.equals(smsCode)) {
            return CommonResult.failed("验证码错误");
        }
        if (!passwordEncoder.matches(password,
                userService.getById(AuthUtils.getUser().getId()).getPassword())) {
            return CommonResult.failed("用户密码错误");
        }
        //验证成功开始更新数据
        User user = new User();
        user.setId(AuthUtils.getUser().getId());
        user.setPhone(phone);
        userService.updateById(user);

        return CommonResult.successNodata("关联手机绑定成功");
    }


    @GetMapping("sendEmail")
    public CommonResult sendEmail(@RequestParam("email") String email) {
        //判断是否和当前手机号一直
        User emailUser = new User();
        emailUser.setEmail(email);
        if (userService.list(Wrappers.query(emailUser)).size() != 0) {
            return CommonResult.failed("新邮箱已注册，不能重复使用");
        }

        String key = RedisConstant.EMAIL_UPDATE_CODE_STR + AuthUtils.getUser().getId();
        //校验是否发送过短信以免重复发送60秒只能发送一次
        if (redisTemplate.hasKey(key)) {
            Long seconds = redisTemplate.opsForValue().getOperations().getExpire(key);
            if (seconds > 0) {
                return CommonResult.failed("已获取过验证码，请等待" + seconds + "秒之后在获取");
            }
        }
        //获取四位随机数字
        String rom = RandomUtil.randomNumbers(4);
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
        redisTemplate.opsForValue().set(RedisConstant.EMAIL_UPDATE_CODE_STR + AuthUtils.getUser().getId(),
                rom + "_" + email, RedisConstant.CODE_TIMEOUT, TimeUnit.SECONDS);
        return CommonResult.successNodata("发送成功");
    }


    @GetMapping("updateEmail")
    public CommonResult updateEmail(@RequestParam("password") String password,
                                    @RequestParam("code") String code) {
        String key = RedisConstant.EMAIL_UPDATE_CODE_STR + AuthUtils.getUser().getId();
        Object smsStr = redisTemplate.opsForValue().get(key);
        if (null == smsStr) {
            return CommonResult.failed("验证码错误");
        }
        String smsCode = String.valueOf(smsStr).split("_")[0];
        String email = String.valueOf(smsStr).split("_")[1];
        if (!code.equals(smsCode)) {
            return CommonResult.failed("验证码错误");
        }
        if (!passwordEncoder.matches(password,
                userService.getById(AuthUtils.getUser().getId()).getPassword())) {
            return CommonResult.failed("用户密码错误");
        }
        //验证成功开始更新数据
        User user = new User();
        user.setId(AuthUtils.getUser().getId());
        user.setEmail(email);
        userService.updateById(user);

        return CommonResult.successNodata("更新绑定邮箱成功");
    }

}
