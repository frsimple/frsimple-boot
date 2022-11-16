package org.simple.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.constant.RedisConstant;
import org.simple.dto.SmsDto;
import org.simple.entity.Sms;
import org.simple.service.SmsService;
import org.simple.utils.CommonResult;
import org.simple.utils.RandomUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * sms管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@AllArgsConstructor
@RequestMapping("/sms")
@Tag(name = "sms", description = "sms管理")
public class SmsController {
    private final SmsService smsService;
    private final RedisTemplate redisTemplate;

    @PostMapping("saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody Sms sms) {
        //防止重复提交
        if (StrUtil.isEmpty(sms.getType())) {
            return CommonResult.failed("数据类型不能为空");
        }
        //查询是否存在已有数据类型的数据
        Sms s1 = new Sms();
        s1.setType(sms.getType());
        List<Sms> smsList = smsService.list(Wrappers.query(s1));
        if (CollectionUtil.isNotEmpty(smsList)) {
            s1 = smsList.get(0);
            sms.setId(s1.getId());
        } else {
            sms.setId(RandomUtil.getSmsCfgId());
        }
        smsService.saveOrUpdate(sms);
        //同步修改缓存数据
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
        return CommonResult.successNodata("修改成功");
    }

    @GetMapping("{type}")
    public CommonResult getOne(@PathVariable("type") String type) {
        Sms sms = new Sms();
        sms.setType(type);
        return CommonResult.success(smsService.getOne(Wrappers.query(sms)));
    }
}
