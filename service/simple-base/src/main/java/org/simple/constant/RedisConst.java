package org.simple.constant;

/**
 * RedisConstant
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public interface RedisConst {

    String CODE_STR = "code_str_";
    String PHONE_CODE_STR = "phone_code_str_";
    String PHONE_UPDATE_CODE_STR = "phone_update_code_str_";
    String EMAIL_UPDATE_CODE_STR = "email_update_code_str_";

    Integer CODE_TIMEOUT = 60;


    String ALIOSS_PIX = "simple:alioss";
    String TENCENT_PIX = "simple:tencentoss";
    String MINIO_PIX = "simple:miniooss";

    String EMIAL_PIX = "simple:email";

    String SMS_ALI = "simple:sms:ali";
    String SMS_TENCENT = "simple:sms:tencent";

}
