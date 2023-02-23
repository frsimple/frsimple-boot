package org.simple.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yitter.idgen.YitIdHelper;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.constant.CommonConst;
import org.simple.constant.RedisConst;
import org.simple.dto.PageResult;
import org.simple.enums.system.ResultCodeEnum;
import org.simple.sms.EmailUtil;
import org.simple.storage.OssUtil;
import org.simple.system.dto.menu.MenuTreeDto;
import org.simple.system.dto.user.UserEntityDto;
import org.simple.system.dto.user.UserQuery;
import org.simple.system.entity.UserEntity;
import org.simple.system.service.IUserService;
import org.simple.utils.ComUtil;
import org.simple.utils.CommonResult;
import org.simple.utils.AuthUtils;
import org.simple.utils.RedisUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/system/user")
@Tag(name = "user", description = "用户管理")
public class UserController {
    private final IUserService userService;
    private final RedisUtil redisUtil;

    @GetMapping("info")
    @Operation(summary = "查询当前用户信息")
    public Map<String, Object> getUserInfo() {
        String userId = AuthUtils.getUserId();
        UserEntity userEntity = userService.getById(userId);
        Map<String, Object> userInfo =
                BeanUtil.beanToMap(userId);
        userInfo.put("avatar", userEntity.getAvatar());
        userInfo.put("nickName", userEntity.getNickName());
        Map<String, Object> result = new HashMap<>(10);
        result.put("roles", StpUtil.getPermissionList(userId));
        result.put("user", userInfo);
        return result;
    }

    @GetMapping("menu")
    @Operation(summary = "查询当前用户菜单")
    public List<MenuTreeDto> getUserMenu() {
        return userService.getUserMenu(AuthUtils.getUserId());
    }

    @GetMapping("list")
    @Operation(summary = "查询用户列表")
    @SaCheckPermission(value = {"system:user:query"}, mode = SaMode.OR)
    public PageResult<UserEntityDto> list(UserQuery query) {
        List<UserEntityDto> list = userService.listAll(query);
        PageResult<UserEntityDto> pageResult = new PageResult<>(list);
        pageResult.setTotal(query.getTotal());
        return pageResult;
    }

    @GetMapping("list1")
    @Operation(summary = "根据条件查询用户")
    public List<UserEntity> list1(UserEntity userEntity) {
        String id = userEntity.getId();
        userEntity.setId(null);
        return userService.list(Wrappers.query(userEntity).notIn("id", id));
    }

    @PostMapping("addUser")
    @Operation(summary = "新增用户信息")
    @SaCheckPermission(value = {"system:user:add"}, mode = SaMode.OR)
    public CommonResult addUser(@RequestBody UserEntityDto userDto) {
        String userid = String.valueOf(YitIdHelper.nextId());
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userid);
        userEntity.setStatus("0");
        userEntity.setErrorCount(0);
        String md5Pwd = DigestUtil.md5Hex(userDto.getUsername().toLowerCase() + userDto.getPassword());
        userEntity.setPassword(md5Pwd);
        userEntity.setPhone(userDto.getPhone());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setNickName(userDto.getNickName());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setOrgan(userDto.getOrgan());
        userService.save(userEntity);
        userService.insertUserTenant(String.valueOf(YitIdHelper.nextId()), userEntity.getTenant(), userid);
        //for循环插入用户角色关联关系数据
        String[] roles = userDto.getRoles().split(CommonConst.STRING_COMMA);
        for (String role : roles) {
            userService.insertRoleUser(String.valueOf(YitIdHelper.nextId()), role, userid);
        }
        return CommonResult.success();
    }

    @PostMapping("editUser")
    @Operation(summary = "修改用户信息")
    @SaCheckPermission(value = {"system:user:edit"}, mode = SaMode.OR)
    public void editUser(@RequestBody UserEntityDto userDto) {
        userService.updateUser(userDto);
    }

    @PostMapping("delUser/{id}")
    @Operation(summary = "删除用户信息")
    @SaCheckPermission(value = {"system:user:del"}, mode = SaMode.OR)
    public CommonResult delUser(@PathVariable("id") String id) {
        if (CommonConst.SUPER_ADMIN.equals(id)) {
            return CommonResult.failed("不允许删除超级管理员");
        }
        return CommonResult.success(userService.delUser(id));
    }

    @GetMapping("lock/{id}")
    @Operation(summary = "锁定用户")
    public CommonResult lock(@PathVariable("id") String id) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setStatus("1");
        userService.updateById(userEntity);
        return CommonResult.success(ResultCodeEnum.A5203.getCode());
    }

    @GetMapping("unlock/{id}")
    @Operation(summary = "解锁用户")
    public CommonResult unlock(@PathVariable("id") String id) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setStatus("0");
        userService.updateById(userEntity);
        return CommonResult.success();
    }

    @GetMapping("resetPwd/{id}")
    @Operation(summary = "重置密码")
    public CommonResult resetPwd(@PathVariable("id") String id) {
        UserEntity userEntity = userService.getById(id);
        String md5Pwd = DigestUtil.md5Hex(userEntity.getUsername().toLowerCase() + "888888");
        userEntity.setPassword(md5Pwd);
        userService.updateById(userEntity);
        return CommonResult.success();
    }

    @PostMapping("updatePwd")
    @Operation(summary = "修改密码")
    public CommonResult updatePwd(@RequestBody UserEntityDto userDto) {
        UserEntity userEntity = new UserEntity();
        if (StrUtil.isEmpty(userDto.getId())) {
            userDto.setId(AuthUtils.getUserId());
        }
        userEntity.setId(userDto.getId());
        String md5Pwd = DigestUtil.md5Hex(userEntity.getUsername().toLowerCase() + userDto.getNPassword());
        userEntity.setPassword(md5Pwd);
        userService.updateById(userEntity);
        return CommonResult.success();
    }

    @PostMapping("checkPwd")
    @Operation(summary = "检验密码是否正确")
    public CommonResult checkPwd(@RequestBody UserEntityDto userDto) {
        if (StrUtil.isEmpty(userDto.getId())) {
            userDto.setId(AuthUtils.getUserId());
        }
        UserEntity userEntity = userService.getById(userDto.getId());
        String md5Pwd = DigestUtil.md5Hex(userDto.getUsername().toLowerCase() + userDto.getNPassword());
        if (!userEntity.getPassword().equals(md5Pwd)) {
            return CommonResult.failed(ResultCodeEnum.A5210.getMsg());
        }
        return CommonResult.success();
    }

    @PostMapping("updateAvatar")
    @Operation(summary = "更新用户头像")
    public CommonResult updateAvatar(@RequestParam("file") MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        File file1 = ComUtil.multipartToFile(file);
        //上传图片
        CommonResult result = OssUtil.getMinioOss(redisUtil).fileUpload(file1, false, AuthUtils.getUserId());
        if (result.getCode() == 0) {
            //上传成功，头像用户头像
            UserEntity userEntity = new UserEntity();
            userEntity.setAvatar(result.getData().toString());
            userEntity.setId(AuthUtils.getUserId());
            userService.updateById(userEntity);
            return CommonResult.success(result.getData().toString());
        } else {
            return result;
        }
    }

    @GetMapping("queryUser")
    @Operation(summary = "查询当前用户")
    public CommonResult queryUser() {
        UserEntity userEntity = userService.getById(AuthUtils.getUserId());
        UserEntity r = new UserEntity();
        r.setId(userEntity.getId());
        r.setEmail(StrUtil.hide(userEntity.getEmail(), 3, 5));
        r.setPhone(DesensitizedUtil.mobilePhone(userEntity.getPhone()));
        r.setNickName(userEntity.getNickName());
        return CommonResult.success(r);
    }

    @PostMapping("updateUser")
    @Operation(summary = "更新用户信息")
    public CommonResult updateUser(@RequestBody UserEntity userEntity) {
        if (!userEntity.getId().equals(AuthUtils.getUserId())) {
            return CommonResult.failed("非法操作");
        }
        return CommonResult.success(userService.updateById(userEntity));
    }

    @GetMapping("sendMsg")
    @Operation(summary = "发送消息")
    public CommonResult sendMsg(@RequestParam("phone") String phone) {
        //判断是否和当前手机号一直
        UserEntity phoneUserEntity = new UserEntity();
        phoneUserEntity.setPhone(phone);
        if (userService.list(Wrappers.query(phoneUserEntity)).size() != 0) {
            return CommonResult.failed("新手机号已注册，不能重复使用");
        }

        String key = RedisConst.PHONE_UPDATE_CODE_STR + AuthUtils.getUserId();
        //校验是否发送过短信以免重复发送60秒只能发送一次
        if (redisUtil.hasKey(key)) {
            Long seconds = redisUtil.getExpire(key);
            if (seconds > 0) {
                return CommonResult.failed("已获取过短信，请等待" + seconds + "秒之后在获取");
            }
        }
        //获取四位随机数字
        String rom = cn.hutool.core.util.RandomUtil.randomNumbers(4);
        try {
            redisUtil.set(RedisConst.PHONE_UPDATE_CODE_STR + AuthUtils.getUserId(),
                    rom + "_" + phone, RedisConst.CODE_TIMEOUT);
        } catch (Exception e) {
            return CommonResult.failed("发送失败：" + e.getMessage());
        }
        return CommonResult.failed("发送失败未配置相关信息");
    }

    @GetMapping("updatePhone")
    @Operation(summary = "更新手机号")
    public CommonResult updatePhone(@RequestParam("password") String password,
                                    @RequestParam("code") String code) {
        String key = RedisConst.PHONE_UPDATE_CODE_STR + AuthUtils.getUserId();
        Object smsStr = redisUtil.get(key);
        if (null == smsStr) {
            return CommonResult.failed("验证码错误");
        }
        String smsCode = String.valueOf(smsStr).split("_")[0];
        String phone = String.valueOf(smsStr).split("_")[1];
        if (!code.equals(smsCode)) {
            return CommonResult.failed("验证码错误");
        }
        UserEntity userEntityInfo = userService.getById(AuthUtils.getUserId());
        String md5Pwd = DigestUtil.md5Hex(userEntityInfo.getUsername().toLowerCase() + password);
        if (!userEntityInfo.getPassword().equals(md5Pwd)) {
            return CommonResult.failed(ResultCodeEnum.A5210.getMsg());
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(AuthUtils.getUserId());
        userEntity.setPhone(phone);
        userService.updateById(userEntity);

        return CommonResult.success();
    }


    @GetMapping("sendEmail")
    @Operation(summary = "发送邮件")
    public CommonResult sendEmail(@RequestParam("email") String email) {
        //判断是否和当前手机号一直
        UserEntity emailUserEntity = new UserEntity();
        emailUserEntity.setEmail(email);
        if (userService.list(Wrappers.query(emailUserEntity)).size() != 0) {
            return CommonResult.failed("新邮箱已注册，不能重复使用");
        }

        String key = RedisConst.EMAIL_UPDATE_CODE_STR + AuthUtils.getUserId();
        //校验是否发送过短信以免重复发送60秒只能发送一次
        if (redisUtil.hasKey(key)) {
            Long seconds = redisUtil.getExpire(key);
            if (seconds > 0) {
                return CommonResult.failed("已获取过验证码，请等待" + seconds + "秒之后在获取");
            }
        }
        //获取四位随机数字
        String rom = cn.hutool.core.util.RandomUtil.randomNumbers(4);
        try {
            CommonResult result = EmailUtil.getInstance(redisUtil)
                    .sendEmail("更换绑定邮箱地址", "验证码：" + rom,
                            new String[]{email}, true, null);
            if (result.getCode() == 1) {
                return CommonResult.failed("发送失败：" + result.getMsg());
            }
        } catch (Exception e) {
            return CommonResult.failed("发送失败：" + e.getMessage());
        }
        redisUtil.set(RedisConst.EMAIL_UPDATE_CODE_STR + AuthUtils.getUserId(), rom + "_" + email, RedisConst.CODE_TIMEOUT);
        return CommonResult.success();
    }


    @GetMapping("updateEmail")
    @Operation(summary = "更新邮箱")
    public CommonResult updateEmail(@RequestParam("password") String password,
                                    @RequestParam("code") String code) {
        String key = RedisConst.EMAIL_UPDATE_CODE_STR + AuthUtils.getUserId();
        Object smsStr = redisUtil.get(key);
        if (null == smsStr) {
            return CommonResult.failed(ResultCodeEnum.A5204.getMsg());
        }
        String smsCode = String.valueOf(smsStr).split("_")[0];
        String email = String.valueOf(smsStr).split("_")[1];
        if (!code.equals(smsCode)) {
            return CommonResult.failed(ResultCodeEnum.A5204.getMsg());
        }
        UserEntity userEntityInfo = userService.getById(AuthUtils.getUserId());
        String md5Pwd = DigestUtil.md5Hex(userEntityInfo.getUsername().toLowerCase() + password);
        if (!userEntityInfo.getPassword().equals(md5Pwd)) {
            return CommonResult.failed(ResultCodeEnum.A5210.getMsg());
        }
        //验证成功开始更新数据
        UserEntity userEntity = new UserEntity();
        userEntity.setId(AuthUtils.getUserId());
        userEntity.setEmail(email);
        userService.updateById(userEntity);
        return CommonResult.success();
    }
}
