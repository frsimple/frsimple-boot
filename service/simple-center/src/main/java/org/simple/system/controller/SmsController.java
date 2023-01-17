package org.simple.system.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yitter.idgen.YitIdHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.constant.RedisConst;
import org.simple.dto.SmsDto;
import org.simple.system.entity.SmsEntity;
import org.simple.system.service.ISmsService;
import org.simple.utils.CommonResult;
import org.simple.utils.RedisUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * sms管理
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/system/sms")
@Tag(name = "sms", description = "sms管理")
public class SmsController {
    private final ISmsService smsService;
    private final RedisUtil redisUtil;

    @PostMapping("saveOrUpdate")
    @Operation(summary = "保存sms配置")
    public CommonResult saveOrUpdate(@RequestBody SmsEntity smsEntity) {
        //防止重复提交
        if (StrUtil.isEmpty(smsEntity.getType())) {
            return CommonResult.failed("数据类型不能为空");
        }
        //查询是否存在已有数据类型的数据
        SmsEntity s1 = new SmsEntity();
        s1.setType(smsEntity.getType());
        List<SmsEntity> smsEntityList = smsService.list(Wrappers.query(s1));
        if (CollectionUtil.isNotEmpty(smsEntityList)) {
            s1 = smsEntityList.get(0);
            smsEntity.setId(s1.getId());
        } else {
            smsEntity.setId(String.valueOf(YitIdHelper.nextId()));
        }
        smsService.saveOrUpdate(smsEntity);
        //同步修改缓存数据
        SmsDto smsDto = new SmsDto();
        smsDto.setAppid(smsEntity.getAppid());
        smsDto.setEndpoint(smsEntity.getEndpoint());
        smsDto.setSecretId(smsEntity.getSecretId());
        smsDto.setSecretKey(smsEntity.getSecretKey());
        smsDto.setSign(smsEntity.getSign());
        if ("ALI".equals(smsEntity.getType())) {
            redisUtil.add(RedisConst.SMS_ALI, BeanUtil.beanToMap(smsDto));
            redisUtil.expire(RedisConst.SMS_ALI, 300000000, TimeUnit.DAYS);
        } else if ("TENCENT".equals(smsEntity.getType())) {
            redisUtil.add(RedisConst.SMS_TENCENT, BeanUtil.beanToMap(smsDto));
            redisUtil.expire(RedisConst.SMS_TENCENT, 300000000, TimeUnit.DAYS);
        }
        return CommonResult.success();
    }

    @GetMapping("{type}")
    @Operation(summary = "获取sms信息")
    public CommonResult getOne(@PathVariable("type") String type) {
        SmsEntity smsEntity = new SmsEntity();
        smsEntity.setType(type);
        return CommonResult.success(smsService.getOne(Wrappers.query(smsEntity)));
    }
}
