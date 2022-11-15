package org.simple.sms;

import cn.hutool.core.bean.BeanUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.apache.commons.lang.StringUtils;
import org.simple.common.constant.RedisConstant;
import org.simple.common.dto.SmsDto;
import org.simple.common.utils.CommonResult;
import org.springframework.data.redis.core.RedisTemplate;

public class TencentSms {

    private static TencentSms tencentSms = null;

    private static SmsDto smsDto ;

    private TencentSms(){}

    public static TencentSms getInstance(RedisTemplate template){
        if(null == tencentSms){
            tencentSms = new TencentSms();
        }
        //设置配置对象
        SmsDto var = BeanUtil.fillBeanWithMap(
                template.opsForHash().entries(RedisConstant.SMS_TENCENT), new SmsDto(),
                false);
        smsDto = var;
        return tencentSms;
    }

    public SmsClient createClient(){
        Credential cred = new Credential(smsDto.getSecretid(), smsDto.getSecretkey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(60);httpProfile.setEndpoint(smsDto.getEndpoint());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        SmsClient client = new SmsClient(cred, smsDto.getRegion(),clientProfile);
        return client;
    }

    /**
     * 国内短信发送
     * */
    public CommonResult sendSms(String sign,String temId,String[] temParams,String[] phoneNumbers){
        SmsClient client = createClient();
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setSmsSdkAppid(smsDto.getAppid());
        sendSmsRequest.setSign(StringUtils.isEmpty(sign)?smsDto.getSign():sign);
        sendSmsRequest.setTemplateID(temId);
        sendSmsRequest.setTemplateParamSet(temParams);
        //循环手机号所有手机号+86
        if(null != phoneNumbers && phoneNumbers.length !=0){
            String[]  numbers = new String[phoneNumbers.length];
            for(int i= 0;i<phoneNumbers.length;i++){
                numbers[i] = "+86"+phoneNumbers[i];
            }
            sendSmsRequest.setPhoneNumberSet(numbers);
        }else{
            sendSmsRequest.setPhoneNumberSet(null);
        }
        try {
            SendSmsResponse resp = client.SendSms(sendSmsRequest);
            if(!"Ok".equals(resp.getSendStatusSet()[0].getCode())){
                return CommonResult.failed("发送失败："+resp.getSendStatusSet()[0].getMessage());
            }
        } catch (TencentCloudSDKException e) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.successNodata("发送成功");
    }
}
