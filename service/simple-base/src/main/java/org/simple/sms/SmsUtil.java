package org.simple.sms;

import org.simple.utils.RedisUtil;

/**
 * SmsUtil
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

public class SmsUtil {

    public static AliSms getAliSms(RedisUtil redisUtil) {
        return AliSms.getInstance(redisUtil);
    }

    public static TencentSms getTencentSms(RedisUtil redisUtil) {
        return TencentSms.getInstance(redisUtil);
    }
}
