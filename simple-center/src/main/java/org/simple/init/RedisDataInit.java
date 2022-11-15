package org.simple.init;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import org.simple.constant.RedisConstant;
import org.simple.dto.EmailDto;
import org.simple.dto.OssDto;
import org.simple.dto.SmsDto;
import org.simple.entity.Email;
import org.simple.entity.Oss;
import org.simple.entity.Sms;
import org.simple.service.EmailService;
import org.simple.service.OssService;
import org.simple.service.SmsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * redis数据初始化
 */
@Configuration
@AllArgsConstructor
public class RedisDataInit {

    private final RedisTemplate redisTemplate;

    private final OssService ossService;

    private final EmailService emailService;

    private final SmsService smsService;


    @PostConstruct
    public void initData() {
        //初始化加载oss数据
        loadOssConfig();
        //初始化加载短信配置，短信模版数据
        loadSmsConfig();
        //初始化邮件配置数据
        loadEmailConfig();
    }


    private void loadSmsConfig() {
        List<Sms> smsList = smsService.list();
        if (CollectionUtil.isNotEmpty(smsList)) {
            smsList.forEach(sms -> {
                SmsDto smsDto = new SmsDto();
                smsDto.setAppid(sms.getAppid());
                smsDto.setEndpoint(sms.getEndpoint());
                smsDto.setSecretid(sms.getSecretid());
                smsDto.setSecretkey(sms.getSecretkey());
                smsDto.setSign(sms.getSign());
                if (sms.getType().equals("ALI")) {
                    redisTemplate.opsForHash().putAll(RedisConstant.SMS_ALI, BeanUtil.beanToMap(smsDto));
                    redisTemplate.expire(RedisConstant.SMS_ALI, 300000000, TimeUnit.DAYS);
                } else if (sms.getType().equals("TENCENT")) {
                    redisTemplate.opsForHash().putAll(RedisConstant.SMS_TENCENT, BeanUtil.beanToMap(smsDto));
                    redisTemplate.expire(RedisConstant.SMS_TENCENT, 300000000, TimeUnit.DAYS);
                }
            });
        }
    }

    private void loadEmailConfig() {
        List<Email> emails = emailService.list();
        if (CollectionUtil.isNotEmpty(emails)) {
            Email email = emails.get(0);
            EmailDto emailDto = new EmailDto();
            emailDto.setHost(email.getHost());
            emailDto.setPort(email.getPort());
            emailDto.setSitename(email.getSitename());
            emailDto.setUsername(email.getUsername());
            emailDto.setPassword(email.getPassword());
            emailDto.setIsssl(email.getIsssl());
            redisTemplate.opsForHash().putAll(RedisConstant.EMIAL_PIX, BeanUtil.beanToMap(emailDto));
            redisTemplate.expire(RedisConstant.EMIAL_PIX, 300000000, TimeUnit.DAYS);
        }
    }

    private void loadOssConfig() {
        List<Oss> ossList = ossService.list();
        if (CollectionUtil.isNotEmpty(ossList)) {
            ossList.forEach(oss -> {
                OssDto o = new OssDto();
                o.setAccesskeysecret(oss.getAccesskeysecret());
                o.setAccesskeyid(oss.getAccesskeyid());
                o.setAppid(oss.getAppid());
                o.setRegion(oss.getRegion());
                o.setEndpoint(oss.getEndpoint());
                o.setWorkspace(oss.getWorkspace());
                if (oss.getType().equals("ALIOSS")) {
                    redisTemplate.opsForHash().putAll(RedisConstant.ALIOSS_PIX, BeanUtil.beanToMap(o));
                    redisTemplate.expire(RedisConstant.ALIOSS_PIX, 300000000, TimeUnit.DAYS);
                } else if (oss.getType().equals("TENCENTCOS")) {
                    redisTemplate.opsForHash().putAll(RedisConstant.TENCENT_PIX, BeanUtil.beanToMap(o));
                    redisTemplate.expire(RedisConstant.TENCENT_PIX, 300000000, TimeUnit.DAYS);
                } else if (oss.getType().equals("MINIO")) {
                    redisTemplate.opsForHash().putAll(RedisConstant.MINIO_PIX, BeanUtil.beanToMap(o));
                    redisTemplate.expire(RedisConstant.MINIO_PIX, 300000000, TimeUnit.DAYS);
                }
            });
        }
    }
}
