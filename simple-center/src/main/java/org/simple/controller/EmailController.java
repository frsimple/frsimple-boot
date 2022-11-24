package org.simple.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.simple.constant.RedisConstant;
import org.simple.dto.EmailDto;
import org.simple.dto.EmailParams;
import org.simple.entity.Email;
import org.simple.service.IEmailService;
import org.simple.sms.EmailUtil;
import org.simple.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@RequestMapping("/email")
@Tag(name = "dict", description = "邮件管理")
public class EmailController {
    @Autowired
    private IEmailService emailService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("emailCfg")
    public List<Email> getEmailCfg() {
        return emailService.list();
    }

    @PostMapping("saveOrUpdate")
    public Boolean saveOrUpdate(@RequestBody Email email) {
        if (StrUtil.isEmpty(email.getId())) {
            email.setId(RandomUtil.getEmailCfgId());
        }
        //初始化缓存
        EmailDto emailDto = new EmailDto();
        emailDto.setHost(email.getHost());
        emailDto.setPort(email.getPort());
        emailDto.setSitename(email.getSitename());
        emailDto.setUsername(email.getUsername());
        emailDto.setPassword(email.getPassword());
        emailDto.setIsssl(email.getIsssl());
        redisTemplate.opsForHash().putAll(RedisConstant.EMIAL_PIX, BeanUtil.beanToMap(emailDto));
        redisTemplate.expire(RedisConstant.EMIAL_PIX, 300000000, TimeUnit.DAYS);
        return emailService.saveOrUpdate(email);
    }

    @PostMapping("sendEmail")
    public Boolean sendEmail(EmailParams emailParams, @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
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
        return EmailUtil.getInstance(redisTemplate).sendEmail(
                emailParams.getTitle(), emailParams.getContent(),
                emailParams.getTos().split(","), true, fileList);
    }
}
