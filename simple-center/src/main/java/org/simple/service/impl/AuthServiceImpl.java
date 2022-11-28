package org.simple.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.AllArgsConstructor;
import org.simple.config.auth.AuthProperties;
import org.simple.constant.RedisConstant;
import org.simple.dto.LoginDto;
import org.simple.dto.LoginParam;
import org.simple.dto.QueryOnlineUserDto;
import org.simple.dto.QueryOnlineUserParam;
import org.simple.entity.User;
import org.simple.enums.system.ResultCode;
import org.simple.exception.CustomException;
import org.simple.service.IAuthService;
import org.simple.service.IUserService;
import org.simple.utils.CommonResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
     * @throws CustomException 异常
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
        StpUtil.getSession().set("userName", userEntity.getUsername());
        StpUtil.getSession().set("nickName", userEntity.getNickname());
        StpUtil.getSession().set("userId", userEntity.getId());
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

    @Override
    public List<QueryOnlineUserDto> getOnlineUser(QueryOnlineUserParam queryOnlineUserParam) {
        // 创建集合
        List<QueryOnlineUserDto> queryOnlineUserDtos = new ArrayList<>();

        // 获取分页后开始的位置，这里减一是因为hutool分页工具的起始算法是从0开始
        int start = PageUtil.getStart(queryOnlineUserParam.getCurrent() - 1, queryOnlineUserParam.getSize());

        // 分页查询数据

        List<String> sessionIdList = StpUtil.searchSessionId("", start, queryOnlineUserParam.getSize(), false);
        for (String sessionId : sessionIdList) {
            SaSession session = StpUtil.getSessionBySessionId(sessionId);
            // 转换登录时间
            Date transformDate = DateUtil.date(session.getCreateTime());
            // 转换剩余时间
            String transformTimeOut = DateUtil.formatBetween(session.getTimeout(), BetweenFormatter.Level.SECOND);
            QueryOnlineUserDto queryOnlineUserDto = QueryOnlineUserDto.builder()
                    .loginDate(transformDate)
                    .timeout(transformTimeOut)
                    .userName(session.getString("userName"))
                    .userId(session.getString("userId"))
                    .nickName(session.getString("nickName")).build();
            queryOnlineUserDtos.add(queryOnlineUserDto);
        }
        return queryOnlineUserDtos;
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
