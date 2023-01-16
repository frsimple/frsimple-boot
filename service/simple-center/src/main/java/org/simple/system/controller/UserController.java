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
import org.simple.utils.ActionResult;
import org.simple.utils.ComUtil;
import org.simple.utils.CurrentUtil;
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
    private final CurrentUtil currentUtil;
    private final RedisUtil redisUtil;

    @GetMapping("info")
    @Operation(summary = "查询当前用户信息")
    public Map<String, Object> getUserInfo() {
        String userId = currentUtil.getCurrentUserId();
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
        return userService.getUserMenu(currentUtil.getCurrentUserId());
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
    public ActionResult addUser(@RequestBody UserEntityDto userDto) {
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
        userService.insertUserTenant(String.valueOf(YitIdHelper.nextId()), userEntity.getTenantId(), userid);
        //for循环插入用户角色关联关系数据
        String[] roles = userDto.getRoles().split(CommonConst.STRING_COMMA);
        for (String role : roles) {
            userService.insertRoleUser(String.valueOf(YitIdHelper.nextId()), role, userid);
        }
        return ActionResult.success();
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
    public ActionResult delUser(@PathVariable("id") String id) {
        if (CommonConst.SUPER_ADMIN.equals(id)) {
            return ActionResult.failed("不允许删除超级管理员");
        }
        return userService.delUser(id);
    }

    @GetMapping("lock/{id}")
    @Operation(summary = "锁定用户")
    public ActionResult lock(@PathVariable("id") String id) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setStatus("1");
        userService.updateById(userEntity);
        return ActionResult.success(ResultCodeEnum.A5203.getCode());
    }

    @GetMapping("unlock/{id}")
    @Operation(summary = "解锁用户")
    public ActionResult unlock(@PathVariable("id") String id) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setStatus("0");
        userService.updateById(userEntity);
        return ActionResult.success();
    }

    @GetMapping("resetPwd/{id}")
    @Operation(summary = "重置密码")
    public ActionResult resetPwd(@PathVariable("id") String id) {
        UserEntity userEntity = userService.getById(id);
        String md5Pwd = DigestUtil.md5Hex(userEntity.getUsername().toLowerCase() + "888888");
        userEntity.setPassword(md5Pwd);
        userService.updateById(userEntity);
        return ActionResult.success();
    }

    @PostMapping("updatePwd")
    @Operation(summary = "修改密码")
    public ActionResult updatePwd(@RequestBody UserEntityDto userDto) {
        UserEntity userEntity = new UserEntity();
        if (StrUtil.isEmpty(userDto.getId())) {
            userDto.setId(currentUtil.getCurrentUserId());
        }
        userEntity.setId(userDto.getId());
        String md5Pwd = DigestUtil.md5Hex(userEntity.getUsername().toLowerCase() + userDto.getNPassword());
        userEntity.setPassword(md5Pwd);
        userService.updateById(userEntity);
        return ActionResult.success();
    }

    @PostMapping("checkPwd")
    @Operation(summary = "检验密码是否正确")
    public ActionResult checkPwd(@RequestBody UserEntityDto userDto) {
        if (StrUtil.isEmpty(userDto.getId())) {
            userDto.setId(currentUtil.getCurrentUserId());
        }
        UserEntity userEntity = userService.getById(userDto.getId());
        String md5Pwd = DigestUtil.md5Hex(userDto.getUsername().toLowerCase() + userDto.getNPassword());
        if (!userEntity.getPassword().equals(md5Pwd)) {
            return ActionResult.failed(ResultCodeEnum.A5210.getCode());
        }
        return ActionResult.success();
    }

    @PostMapping("updateAvatar")
    @Operation(summary = "更新用户头像")
    public ActionResult updateAvatar(@RequestParam("file") MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        File file1 = ComUtil.multipartToFile(file);
        //上传图片
        ActionResult result = OssUtil.getMinioOss(redisUtil).fileUpload(file1, false, currentUtil.getCurrentUserId());
        if (result.getStatusCode().equals("200")) {
            //上传成功，头像用户头像
            UserEntity userEntity = new UserEntity();
            userEntity.setAvatar(result.getData().toString());
            userEntity.setId(currentUtil.getCurrentUserId());
            userService.updateById(userEntity);
            return ActionResult.success(result.getData().toString());
        } else {
            return result;
        }
    }

    @GetMapping("queryUser")
    @Operation(summary = "查询当前用户")
    public ActionResult queryUser() {
        UserEntity userEntity = userService.getById(currentUtil.getCurrentUserId());
        UserEntity r = new UserEntity();
        r.setId(userEntity.getId());
        r.setEmail(StrUtil.hide(userEntity.getEmail(), 3, 5));
        r.setPhone(DesensitizedUtil.mobilePhone(userEntity.getPhone()));
        r.setNickName(userEntity.getNickName());
        return ActionResult.success(r);
    }

    @PostMapping("updateUser")
    @Operation(summary = "更新用户信息")
    public ActionResult updateUser(@RequestBody UserEntity userEntity) {
        if (!userEntity.getId().equals(currentUtil.getCurrentUserId())) {
            return ActionResult.failed("非法操作");
        }
        return ActionResult.success(userService.updateById(userEntity));
    }

    @GetMapping("sendMsg")
    @Operation(summary = "发送消息")
    public ActionResult sendMsg(@RequestParam("phone") String phone) {
        //判断是否和当前手机号一直
        UserEntity phoneUserEntity = new UserEntity();
        phoneUserEntity.setPhone(phone);
        if (userService.list(Wrappers.query(phoneUserEntity)).size() != 0) {
            return ActionResult.failed("新手机号已注册，不能重复使用");
        }

        String key = RedisConst.PHONE_UPDATE_CODE_STR + currentUtil.getCurrentUserId();
        //校验是否发送过短信以免重复发送60秒只能发送一次
        if (redisUtil.hasKey(key)) {
            Long seconds = redisUtil.getExpire(key);
            if (seconds > 0) {
                return ActionResult.failed("已获取过短信，请等待" + seconds + "秒之后在获取");
            }
        }
        //获取四位随机数字
        String rom = cn.hutool.core.util.RandomUtil.randomNumbers(4);
        try {
            redisUtil.set(RedisConst.PHONE_UPDATE_CODE_STR + currentUtil.getCurrentUserId(),
                    rom + "_" + phone, RedisConst.CODE_TIMEOUT);
        } catch (Exception e) {
            return ActionResult.failed("发送失败：" + e.getMessage());
        }
        return ActionResult.failed("发送失败未配置相关信息");
    }

    @GetMapping("updatePhone")
    @Operation(summary = "更新手机号")
    public ActionResult updatePhone(@RequestParam("password") String password,
                                    @RequestParam("code") String code) {
        String key = RedisConst.PHONE_UPDATE_CODE_STR + currentUtil.getCurrentUserId();
        Object smsStr = redisUtil.get(key);
        if (null == smsStr) {
            return ActionResult.failed("验证码错误");
        }
        String smsCode = String.valueOf(smsStr).split("_")[0];
        String phone = String.valueOf(smsStr).split("_")[1];
        if (!code.equals(smsCode)) {
            return ActionResult.failed("验证码错误");
        }
        UserEntity userEntityInfo = userService.getById(currentUtil.getCurrentUserId());
        String md5Pwd = DigestUtil.md5Hex(userEntityInfo.getUsername().toLowerCase() + password);
        if (!userEntityInfo.getPassword().equals(md5Pwd)) {
            return ActionResult.failed(ResultCodeEnum.A5210.getCode());
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(currentUtil.getCurrentUserId());
        userEntity.setPhone(phone);
        userService.updateById(userEntity);

        return ActionResult.success();
    }


    @GetMapping("sendEmail")
    @Operation(summary = "发送邮件")
    public ActionResult sendEmail(@RequestParam("email") String email) {
        //判断是否和当前手机号一直
        UserEntity emailUserEntity = new UserEntity();
        emailUserEntity.setEmail(email);
        if (userService.list(Wrappers.query(emailUserEntity)).size() != 0) {
            return ActionResult.failed("新邮箱已注册，不能重复使用");
        }

        String key = RedisConst.EMAIL_UPDATE_CODE_STR + currentUtil.getCurrentUserId();
        //校验是否发送过短信以免重复发送60秒只能发送一次
        if (redisUtil.hasKey(key)) {
            Long seconds = redisUtil.getExpire(key);
            if (seconds > 0) {
                return ActionResult.failed("已获取过验证码，请等待" + seconds + "秒之后在获取");
            }
        }
        //获取四位随机数字
        String rom = cn.hutool.core.util.RandomUtil.randomNumbers(4);
        try {
            ActionResult result = EmailUtil.getInstance(redisUtil)
                    .sendEmail("更换绑定邮箱地址", "验证码：" + rom,
                            new String[]{email}, true, null);
            if (!result.getStatusCode().equals("200")) {
                return ActionResult.failed("发送失败：" + result.getMessage());
            }
        } catch (Exception e) {
            return ActionResult.failed("发送失败：" + e.getMessage());
        }
        redisUtil.set(RedisConst.EMAIL_UPDATE_CODE_STR + currentUtil.getCurrentUserId(), rom + "_" + email, RedisConst.CODE_TIMEOUT);
        return ActionResult.success();
    }


    @GetMapping("updateEmail")
    @Operation(summary = "更新邮箱")
    public ActionResult updateEmail(@RequestParam("password") String password,
                                    @RequestParam("code") String code) {
        String key = RedisConst.EMAIL_UPDATE_CODE_STR + currentUtil.getCurrentUserId();
        Object smsStr = redisUtil.get(key);
        if (null == smsStr) {
            return ActionResult.failed(ResultCodeEnum.A5204.getCode());
        }
        String smsCode = String.valueOf(smsStr).split("_")[0];
        String email = String.valueOf(smsStr).split("_")[1];
        if (!code.equals(smsCode)) {
            return ActionResult.failed(ResultCodeEnum.A5204.getCode());
        }
        UserEntity userEntityInfo = userService.getById(currentUtil.getCurrentUserId());
        String md5Pwd = DigestUtil.md5Hex(userEntityInfo.getUsername().toLowerCase() + password);
        if (!userEntityInfo.getPassword().equals(md5Pwd)) {
            return ActionResult.failed(ResultCodeEnum.A5210.getCode());
        }
        //验证成功开始更新数据
        UserEntity userEntity = new UserEntity();
        userEntity.setId(currentUtil.getCurrentUserId());
        userEntity.setEmail(email);
        userService.updateById(userEntity);
        return ActionResult.success();
    }
}
