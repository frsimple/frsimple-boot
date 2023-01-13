package org.simple.system.init;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.simple.constant.CommonConst;
import org.simple.constant.RedisConst;
import org.simple.dto.EmailDto;
import org.simple.dto.OssDto;
import org.simple.dto.SmsDto;
import org.simple.system.entity.DictionaryEntity;
import org.simple.system.entity.EmailEntity;
import org.simple.system.entity.OssEntity;
import org.simple.system.entity.SmsEntity;
import org.simple.system.service.IDictionaryService;
import org.simple.system.service.IEmailService;
import org.simple.system.service.IOssService;
import org.simple.system.service.ISmsService;
import org.simple.utils.RedisUtil;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis数据初始化
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Configuration
@AllArgsConstructor
public class RedisDataInit {

    private final RedisUtil redisUtil;
    private final IOssService ossService;
    private final IEmailService emailService;
    private final ISmsService smsService;
    private final IDictionaryService dictionaryService;


    @PostConstruct
    public void initData() {
        //初始化加载oss数据
        loadOssConfig();
        //初始化加载短信配置，短信模版数据
        loadSmsConfig();
        //初始化邮件配置数据
        loadEmailConfig();
        //初始化字典缓存
        loadDictConfig();
    }

    private void loadDictConfig() {
        DictionaryEntity dictionaryEntity = new DictionaryEntity();
        dictionaryEntity.setValue("#");
        List<DictionaryEntity> dictionaryEntityList = dictionaryService.list(Wrappers.query(dictionaryEntity));
        if (dictionaryEntityList.size() != 0) {
            for (DictionaryEntity item : dictionaryEntityList) {
                DictionaryEntity d = new DictionaryEntity();
                d.setCode(item.getCode());
                List<DictionaryEntity> dicts =
                        dictionaryService.list(Wrappers.query(d).notIn("value", "#"));
                JSONArray array = new JSONArray();
                if (dicts.size() != 0) {
                    for (DictionaryEntity item1 : dicts) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("value", item1.getValue());
                        jsonObject.put("id", item1.getId());
                        jsonObject.put("label", item1.getLabel());
                        jsonObject.put("code", item1.getCode());
                        array.add(jsonObject);
                    }
                }
                redisUtil.set(item.getCode(), String.valueOf(array));
            }
        }
    }


    private void loadSmsConfig() {
        List<SmsEntity> smsEntityList = smsService.list();
        if (CollectionUtil.isNotEmpty(smsEntityList)) {
            smsEntityList.forEach(sms -> {
                SmsDto smsDto = new SmsDto();
                smsDto.setAppid(sms.getAppid());
                smsDto.setEndpoint(sms.getEndpoint());
                smsDto.setSecretId(sms.getSecretId());
                smsDto.setSecretKey(sms.getSecretKey());
                smsDto.setSign(sms.getSign());
                if ("ALI".equals(sms.getType())) {
                    redisUtil.add(RedisConst.SMS_ALI, BeanUtil.beanToMap(smsDto));
                    redisUtil.expire(RedisConst.SMS_ALI, 300000000, TimeUnit.DAYS);
                } else if ("TENCENT".equals(sms.getType())) {
                    redisUtil.add(RedisConst.SMS_TENCENT, BeanUtil.beanToMap(smsDto));
                    redisUtil.expire(RedisConst.SMS_TENCENT, 300000000, TimeUnit.DAYS);
                }
            });
        }
    }

    private void loadEmailConfig() {
        List<EmailEntity> emailEntities = emailService.list();
        if (CollectionUtil.isNotEmpty(emailEntities)) {
            EmailEntity emailEntity = emailEntities.get(0);
            EmailDto emailDto = new EmailDto();
            emailDto.setHost(emailEntity.getHost());
            emailDto.setPort(emailEntity.getPort());
            emailDto.setSiteName(emailEntity.getSiteName());
            emailDto.setUsername(emailEntity.getUsername());
            emailDto.setPassword(emailEntity.getPassword());
            emailDto.setIsSsl(emailEntity.getIsSsl());
            redisUtil.add(RedisConst.EMIAL_PIX, BeanUtil.beanToMap(emailDto));
            redisUtil.expire(RedisConst.EMIAL_PIX, 300000000, TimeUnit.DAYS);
        }
    }

    private void loadOssConfig() {
        List<OssEntity> ossEntityList = ossService.list();
        if (CollectionUtil.isNotEmpty(ossEntityList)) {
            ossEntityList.forEach(oss -> {
                OssDto o = new OssDto();
                o.setAccessKeySecret(oss.getAccessKeySecret());
                o.setAccessKeyId(oss.getAccessKeyId());
                o.setAppid(oss.getAppid());
                o.setRegion(oss.getRegion());
                o.setEndpoint(oss.getEndpoint());
                o.setWorkspace(oss.getWorkspace());
                if (CommonConst.MINIO.equals(oss.getType())) {
                    redisUtil.add(RedisConst.MINIO_PIX, BeanUtil.beanToMap(o));
                    redisUtil.expire(RedisConst.MINIO_PIX, 300000000, TimeUnit.DAYS);
                }
            });
        }
    }
}
