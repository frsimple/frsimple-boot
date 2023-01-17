package org.simple.sms;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.simple.constant.RedisConst;
import org.simple.dto.SmsDto;
import org.simple.enums.system.ResultCodeEnum;
import org.simple.utils.CommonResult;
import org.simple.utils.RedisUtil;

/**
 * TencentSms
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public class TencentSms {

    private static TencentSms tencentSms = null;

    private static SmsDto smsDto;

    private TencentSms() {
    }

    public static TencentSms getInstance(RedisUtil redisUtil) {
        if (null == tencentSms) {
            tencentSms = new TencentSms();
        }
        //设置配置对象
        smsDto = BeanUtil.fillBeanWithMap(
                redisUtil.entries(RedisConst.SMS_TENCENT), new SmsDto(),
                false);
        return tencentSms;
    }

    public SmsClient createClient() {
        Credential cred = new Credential(smsDto.getSecretId(), smsDto.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(60);
        httpProfile.setEndpoint(smsDto.getEndpoint());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, smsDto.getRegion(), clientProfile);
    }

    /**
     * 国内短信发送
     */
    public CommonResult<?> sendSms(String sign, String temId, String[] temParams, String[] phoneNumbers) {
        SmsClient client = createClient();
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setSmsSdkAppid(smsDto.getAppid());
        sendSmsRequest.setSign(StrUtil.isEmpty(sign) ? smsDto.getSign() : sign);
        sendSmsRequest.setTemplateID(temId);
        sendSmsRequest.setTemplateParamSet(temParams);
        //循环手机号所有手机号+86
        if (null != phoneNumbers && phoneNumbers.length != 0) {
            String[] numbers = new String[phoneNumbers.length];
            for (int i = 0; i < phoneNumbers.length; i++) {
                numbers[i] = "+86" + phoneNumbers[i];
            }
            sendSmsRequest.setPhoneNumberSet(numbers);
        } else {
            sendSmsRequest.setPhoneNumberSet(null);
        }
        try {
            SendSmsResponse resp = client.SendSms(sendSmsRequest);
            if (!"Ok".equals(resp.getSendStatusSet()[0].getCode())) {
                return CommonResult.failed(ResultCodeEnum.FAILED.getMsg());
            }
        } catch (TencentCloudSDKException e) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success(ResultCodeEnum.SUCCESS.getCode());
    }
}
