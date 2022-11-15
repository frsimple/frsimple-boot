package org.simple.sms;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import org.simple.constant.RedisConstant;
import org.simple.dto.EmailDto;
import org.simple.utils.CommonResult;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;

public class EmailUtil {

    private static EmailDto emailDto;
    private static EmailUtil emailUtil = null;
    private EmailUtil(){
    }

    public static EmailUtil getInstance(RedisTemplate redisTemplate){
        if(null == emailUtil){
            emailUtil = new EmailUtil();
        }
        //设置配置对象
        EmailDto var = BeanUtil.fillBeanWithMap(
                redisTemplate.opsForHash().entries(RedisConstant.EMIAL_PIX), new EmailDto(),
                false);
        emailDto = var;
        return emailUtil;
    }

    /**
     * 发送邮件
     * */
    public CommonResult sendEmail(String title, String content, String[] tos, boolean isHtml, File[] files){
         try{
             MailAccount account = new MailAccount();
             account.setHost(emailDto.getHost());
             account.setPort(Integer.valueOf(emailDto.getPort()));
             account.setAuth(true);
             account.setFrom(emailDto.getSitename()+"<"+emailDto.getUsername()+">");
             account.setUser(emailDto.getUsername());
             account.setPass(emailDto.getPassword());
             account.setSslEnable(emailDto.getIsssl() == 1);
             if(null == files || files.length == 0){
                 MailUtil.send(account, CollUtil.newArrayList(tos),title,content,isHtml);
             }else{
                 MailUtil.send(account,CollUtil.newArrayList(tos),title,content,isHtml,files);
             }
             return CommonResult.success("发送成功");
         }catch (Exception ex){
             return CommonResult.failed("发送失败："+ex.getMessage());
         }
    }
}
