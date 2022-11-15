package org.simple.sms;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * SmsUtil
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

public class SmsUtil {

    public static AliSms getAliSms(RedisTemplate redisTemplate) {
        return AliSms.getInstance(redisTemplate);
    }

    public static TencentSms getTencentSms(RedisTemplate redisTemplate) {
        return TencentSms.getInstance(redisTemplate);
    }
}
