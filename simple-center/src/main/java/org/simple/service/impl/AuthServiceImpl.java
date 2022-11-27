package org.simple.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.simple.config.auth.AuthProperties;
import org.simple.constant.IErrorCode;
import org.simple.constant.RedisConstant;
import org.simple.dto.LoginDto;
import org.simple.dto.LoginParam;
import org.simple.entity.User;
import org.simple.enums.system.ResultCode;
import org.simple.exception.CustomException;
import org.simple.service.IAuthService;
import org.simple.service.IUserService;
import org.simple.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * LoginServiceImpl
 *
 * @author xiaozhi
 * @version v1.0
 * @since 2022-11-24 22:26:08
 */
@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IUserService userService;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthProperties authProperties;

    /**
     * 通过用户名登录
     *
     * @param loginParam 登录参数
     * @return 返回登录实体信息
     */
    @Override
    public LoginDto loginByUserName(LoginParam loginParam) throws CustomException {
        // 验证验证码是否过期或者输入正确
        checkIsValidCode(loginParam.getCode(), loginParam.getSp());
        User userEntity = BeanUtil.toBean(checkLogin(loginParam).getData(), User.class);
        // 所有验证通过后开始登录
        StpUtil.login(userEntity.getId(), loginParam.getDevice());
        // 登录后获取token信息
        String token = StpUtil.getTokenValue();
        return LoginDto.builder().token(token).build();
    }

    /**
     * 获取当前登录人id
     *
     * @return 登录人id
     */
    @Override
    public String getCurrentUserId() {
        return StpUtil.getLoginId().toString();
    }

    /**
     * 获取当前登录人-token
     *
     * @return token
     */
    @Override
    public String getCurrentToken() {
        return StpUtil.getTokenValue();
    }

    /**
     * 校验验证码
     *
     * @param code 验证码
     * @param sp   时间
     */
    public void checkIsValidCode(String code, String sp) {
        Object redisCode = redisTemplate.opsForValue().get(RedisConstant.CODE_STR + sp);
        Assert.isTrue(ObjectUtil.isNotNull(redisCode), "验证码已过期");
        redisTemplate.delete(RedisConstant.CODE_STR + sp);
        Assert.isTrue(StrUtil.equals(String.valueOf(redisCode), code), "验证码错误");
    }

    /**
     * 登录验证
     *
     * @param inputVO 登录参数
     * @return 当前用户
     */
    public CommonResult checkLogin(LoginParam inputVO) throws CustomException {
        User entity = userService.getInfoByUserName(inputVO.getUserName());
        if (entity == null) {
            throw new CustomException(ResultCode.A0201.getCode());
        }
        int errorCount = ObjectUtil.isNotNull(entity.getErrorcount()) ? entity.getErrorcount() : 0;
        if (errorCount >= authProperties.getErrorNumber()) {
            throw new CustomException(ResultCode.A0211.getCode());
        }
        DateTime lockTime = DateUtil.date().offset(DateField.MINUTE, authProperties.getLockTime());
        if (ObjectUtil.isNotNull(entity.getLocktime()) && DateUtil.date().isAfter(lockTime)) {
            throw new CustomException(ResultCode.A0202.getCode());
        }
        String md5Pwd = DigestUtil.md5Hex(inputVO.getUserName().toLowerCase() + inputVO.getPassword());
        if (!entity.getPassword().equals(md5Pwd)) {
            entity.setErrorcount(errorCount + 1);
            entity.setLocktime(DateTime.now().toLocalDateTime());
            userService.updateById(entity);
            throw new CustomException(ResultCode.A0210.getCode());
        }
        return CommonResult.success(entity);
    }
}
