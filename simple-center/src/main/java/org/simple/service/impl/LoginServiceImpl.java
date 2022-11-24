package org.simple.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.simple.constant.RedisConstant;
import org.simple.dto.LoginDto;
import org.simple.dto.LoginParam;
import org.simple.entity.User;
import org.simple.service.ILoginService;
import org.simple.service.IUserService;
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
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     * 通过用户名登录
     *
     * @param loginParam 登录参数
     * @return 返回登录实体信息
     */
    @Override
    public LoginDto loginByUserName(LoginParam loginParam) {
        // 验证验证码是否过期或者输入正确
        checkIsValidCode(loginParam.getCode(),loginParam.getSp());
        // 查询用户信息
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getUsername,loginParam.getUserName());
        User selectUser = userService.getOne(queryWrapper);
        // 判断用户信息是否为空
        Assert.isTrue(ObjectUtil.isNotNull(selectUser),"用户名或密码错误");
        // 判断输入的密码是否正确
        Assert.isTrue(StrUtil.equals(loginParam.getPassword(),selectUser.getPassword()),"用户名或密码错误");
        /**
         * TODO: 尚未实现的等保需求：1、密码输入错误限制次；2、如果是第一次初始化密码强制修改密码
         */
        // 所有验证通过后开始登录
        StpUtil.login(selectUser.getId(),loginParam.getDevice());
        // 登录后获取token信息
        String token = StpUtil.getTokenValue();
        return LoginDto.builder().token(token).build();
    }

    public void checkIsValidCode (String code,String sp){
        Object  redisCode  =  redisTemplate.opsForValue().get(RedisConstant.CODE_STR+sp);
        Assert.isTrue(ObjectUtil.isNotNull(redisCode),"验证码已过期");
        redisTemplate.delete(RedisConstant.CODE_STR+sp);
        Assert.isTrue(StrUtil.equals(String.valueOf(redisCode),code),"验证码错误");
    }
}
