package org.simple.sms;

import org.springframework.data.redis.core.RedisTemplate;

public class SmsUtil {

    public static AliSms getAliSms(RedisTemplate redisTemplate){
        return AliSms.getInstance(redisTemplate);
    }

    public static TencentSms getTencentSms(RedisTemplate redisTemplate){
        return TencentSms.getInstance(redisTemplate);
    }
}
