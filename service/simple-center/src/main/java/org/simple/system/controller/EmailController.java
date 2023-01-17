package org.simple.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.github.yitter.idgen.YitIdHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.constant.RedisConst;
import org.simple.dto.EmailDto;
import org.simple.sms.EmailUtil;
import org.simple.system.dto.email.EmailQuery;
import org.simple.system.entity.EmailEntity;
import org.simple.system.service.IEmailService;
import org.simple.utils.CommonResult;
import org.simple.utils.RedisUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 邮件管理
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@AllArgsConstructor
@RequestMapping("/center/system/email")
@Tag(name = "dict", description = "邮件管理")
public class EmailController {
    private final IEmailService emailService;
    private final RedisUtil redisUtil;

    @GetMapping("emailCfg")
    @Operation(summary = "获取邮件配置")
    public List<EmailEntity> getEmailCfg() {
        return emailService.list();
    }

    @PostMapping("saveOrUpdate")
    @Operation(summary = "保存邮箱配置")
    public CommonResult saveOrUpdate(@RequestBody EmailEntity emailEntity) {
        if (StrUtil.isEmpty(emailEntity.getId())) {
            emailEntity.setId(String.valueOf(YitIdHelper.nextId()));
        }
        //初始化缓存
        EmailDto emailDto = new EmailDto();
        emailDto.setHost(emailEntity.getHost());
        emailDto.setPort(emailEntity.getPort());
        emailDto.setSiteName(emailEntity.getSiteName());
        emailDto.setUsername(emailEntity.getUsername());
        emailDto.setPassword(emailEntity.getPassword());
        emailDto.setIsSsl(emailEntity.getIsSsl());
        redisUtil.add(RedisConst.EMIAL_PIX, BeanUtil.beanToMap(emailDto));
        redisUtil.expire(RedisConst.EMIAL_PIX, 300000000, TimeUnit.DAYS);
        emailService.saveOrUpdate(emailEntity);
        return CommonResult.success();
    }

    @PostMapping("sendEmail")
    @Operation(summary = "发送邮件")
    public CommonResult sendEmail(EmailQuery emailQuery, @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
        File[] fileList = new File[files.length];
        if (null != files && files.length != 0) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                try {
                    File f = null;
                    InputStream inputStream = file.getInputStream();
                    String originalFilename = file.getOriginalFilename();
                    f = new File(originalFilename);
                    FileUtil.writeFromStream(inputStream, f);
                    fileList[i] = f;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return EmailUtil.getInstance(redisUtil).sendEmail(
                emailQuery.getTitle(), emailQuery.getContent(),
                emailQuery.getTos().split(","), true, fileList);
    }
}
