package org.simple.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.simple.constant.RedisConstant;
import org.simple.dto.FileDto;
import org.simple.dto.OssDto;
import org.simple.entity.Oss;
import org.simple.service.IOssService;
import org.simple.storage.OssUtil;
import org.simple.utils.CommonResult;
import org.simple.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * oss管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@RequestMapping("/center/oss")
@Tag(name = "oss", description = "oss管理")
public class OssController {

    @Autowired
    private  RedisTemplate redisTemplate;
    @Autowired
    private  IOssService ossService;

    @GetMapping("{type}")
    public Oss getOss(@PathVariable("type") String type) {
        Oss o = new Oss();
        o.setType(type);
        return ossService.getOne(Wrappers.query(o));
    }

    @PostMapping("saveOrUpdate")
    public Boolean saveOrUpdate(@RequestBody Oss oss) {
        Oss query = new Oss();
        query.setType(oss.getType());
        query = ossService.getOne(Wrappers.query(query));
        if (null != query) {
            oss.setId(query.getId());
            oss.setUpdatetime(LocalDateTime.now());
        } else {
            oss.setId(RandomUtil.getOssId());
            oss.setCreatetime(LocalDateTime.now());
            oss.setUpdatetime(LocalDateTime.now());
        }
        ossService.saveOrUpdate(oss);
        //刷新缓存
        OssDto ossDto = new OssDto();
        ossDto.setAccesskeysecret(oss.getAccesskeysecret());
        ossDto.setAccesskeyid(oss.getAccesskeyid());
        ossDto.setAppid(oss.getAppid());
        ossDto.setRegion(oss.getRegion());
        ossDto.setEndpoint(oss.getEndpoint());
        ossDto.setWorkspace(oss.getWorkspace());
        if (oss.getType().equals("ALIOSS")) {
            redisTemplate.opsForHash().putAll(RedisConstant.ALIOSS_PIX, BeanUtil.beanToMap(ossDto));
            redisTemplate.expire(RedisConstant.ALIOSS_PIX, 300000000, TimeUnit.DAYS);
        } else if (oss.getType().equals("TENCENTCOS")) {
            redisTemplate.opsForHash().putAll(RedisConstant.TENCENT_PIX, BeanUtil.beanToMap(ossDto));
            redisTemplate.expire(RedisConstant.TENCENT_PIX, 300000000, TimeUnit.DAYS);
        } else if (oss.getType().equals("MINIO")) {
            redisTemplate.opsForHash().putAll(RedisConstant.MINIO_PIX, BeanUtil.beanToMap(ossDto));
            redisTemplate.expire(RedisConstant.MINIO_PIX, 300000000, TimeUnit.DAYS);
        }
        return ossService.saveOrUpdate(oss);
    }

    @GetMapping("listFiles/{type}")
    public CommonResult<?> listFiles(@PathVariable("type") String type,
                                     @RequestParam("prefix") String prefix,
                                     @RequestParam(value = "nextmarker", required = false) String nextmarker) {
        if (type.equals("ALIOSS")) {
            return CommonResult.success(OssUtil.getAliOss(redisTemplate).listFiles(50, nextmarker, prefix));
        } else if (type.equals("TENCENTCOS")) {
            return CommonResult.success(OssUtil.getTencentOss(redisTemplate).listFiles(50, nextmarker, prefix));
        } else if (type.equals("MINIO")) {
            return CommonResult.success(OssUtil.getMinioOss(redisTemplate).listFiles(50, nextmarker, prefix));
        }
        return CommonResult.success(new ArrayList<>());
    }

    @GetMapping("downloadFile/{type}")
    public void downloadFile(@PathVariable("type") String type,
                             @RequestParam("key") String key, HttpServletResponse response) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        FileDto fIleDto = new FileDto();
        if (type.equals("ALIOSS")) {
            fIleDto = OssUtil.getAliOss(redisTemplate).downLoad(key);
        } else if (type.equals("TENCENTCOS")) {
            fIleDto = OssUtil.getTencentOss(redisTemplate).downLoad(key);
        } else if (type.equals("MINIO")) {
            fIleDto = OssUtil.getMinioOss(redisTemplate).downLoad(key);
        }
        try {
            //通知浏览器以附件形式下载
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(key, "utf-8"));
            IoUtil.write(response.getOutputStream(), Boolean.TRUE, fIleDto.getFileBytes());
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.getOutputStream().close();
        }
    }

    @GetMapping("downloadFileLink/{type}")
    public String downloadFileLink(@PathVariable("type") String type,
                                   @RequestParam("key") String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String result = "";
        if (type.equals("ALIOSS")) {
            result = OssUtil.getAliOss(redisTemplate).downLoadLink(key, 100L);
        } else if (type.equals("TENCENTCOS")) {
            result = OssUtil.getTencentOss(redisTemplate).downLoadLink(key, 100L);
        } else if (type.equals("MINIO")) {
            result = OssUtil.getMinioOss(redisTemplate).downLoadLink(key, 100);
        }
        return result;
    }
}
