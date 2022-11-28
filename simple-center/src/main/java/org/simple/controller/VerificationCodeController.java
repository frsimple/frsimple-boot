package org.simple.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.constant.RedisConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码控制器
 *
 * @author xiaozhi
 * @version v1.0
 * @since 2022-11-24 22:07:08
 */
@SaIgnore
@RestController
@AllArgsConstructor
@RequestMapping("/center/code")
@Tag(name = "code", description = "验证码")
public class VerificationCodeController {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 获取验证码图片
     *
     * @param response        响应
     * @param uniqueTimeStamp 唯一时间戳
     * @throws IOException 异常信息
     */
    @GetMapping
    @Operation(summary = "获取验证码")
    public void getCode(HttpServletResponse response, @RequestParam("sp") String uniqueTimeStamp) throws IOException {
        //生成验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        captcha.setLen(2);
        String result;
        try {
            result = new Double(Double.parseDouble(captcha.text())).intValue() + "";
        } catch (Exception e) {
            result = captcha.text();
        }
        //存储验证码到redis中
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(RedisConstant.CODE_STR + uniqueTimeStamp, result
                , RedisConstant.CODE_TIMEOUT, TimeUnit.SECONDS);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        // 为了设置验证码课实时刷新
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        ServletOutputStream outputStream = response.getOutputStream();
        captcha.out(outputStream);
    }
}
