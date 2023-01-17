package org.simple.sms;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import org.simple.constant.CommonConst;
import org.simple.constant.RedisConst;
import org.simple.dto.SmsDto;
import org.simple.enums.system.ResultCodeEnum;
import org.simple.utils.CommonResult;
import org.simple.utils.RedisUtil;

/**
 * sms
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public class AliSms {

    private static AliSms aliSms = null;

    private static SmsDto smsDto;

    private AliSms() {
    }

    public static AliSms getInstance(RedisUtil redisUtil) {
        if (null == aliSms) {
            aliSms = new AliSms();
        }
        //设置配置对象
        smsDto = BeanUtil.fillBeanWithMap(
                redisUtil.entries(RedisConst.SMS_ALI), new SmsDto(),
                false);
        return aliSms;
    }

    public static com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        Config config = new Config()
                // 您的 AccessKey ID
                .setAccessKeyId(smsDto.getSecretId())
                // 您的 AccessKey Secret
                .setAccessKeySecret(smsDto.getSecretKey());
        // 访问的域名
        if (StrUtil.isNotEmpty(smsDto.getEndpoint())) {
            config.endpoint = smsDto.getEndpoint();
        } else {
            config.endpoint = "dysmsapi.aliyuncs.com";
        }
        if (StrUtil.isNotEmpty(smsDto.getRegion())) {
            config.regionId = smsDto.getRegion();
        }
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    /**
     * 发送同步短信
     */
    public CommonResult<?> sendAsyncSms(String signName, String[] phoneNumbers,
                                        String temId, JSONObject temParams) throws Exception {
        try {
            com.aliyun.dysmsapi20170525.Client client = createClient();
            SendSmsRequest sendSmsRequest = new SendSmsRequest();
            sendSmsRequest.setPhoneNumbers(StrUtil.join(CommonConst.STRING_COMMA, phoneNumbers));
            sendSmsRequest.setSignName(StrUtil.isEmpty(signName) ? smsDto.getSign() : signName);
            sendSmsRequest.setTemplateCode(temId);
            sendSmsRequest.setTemplateParam(JSONObject.toJSONString(temParams));
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            if (!"OK".equals(sendSmsResponse.getBody().getCode())) {
                return CommonResult.failed(ResultCodeEnum.FAILED.getMsg());
            }
        } catch (Exception ex) {
            return CommonResult.failed(ResultCodeEnum.FAILED.getMsg());
        }

        return CommonResult.success(ResultCodeEnum.SUCCESS.getCode());
    }
}
