package org.simple.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.constant.RedisConstant;
import org.simple.dto.FileDto;
import org.simple.dto.OssDto;
import org.simple.entity.Oss;
import org.simple.service.IOssService;
import org.simple.storage.OssUtil;
import org.simple.utils.CommonResult;
import org.simple.utils.RandomUtil;
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
@AllArgsConstructor
@RequestMapping("/center/oss")
@Tag(name = "oss", description = "oss管理")
public class OssController {

    private final RedisTemplate redisTemplate;
    private final IOssService ossService;

    @GetMapping("{type}")
    @Operation(summary = "获取oss配置")
    public Oss getOss(@PathVariable("type") String type) {
        Oss o = new Oss();
        o.setType(type);
        return ossService.getOne(Wrappers.query(o));
    }

    @PostMapping("saveOrUpdate")
    @Operation(summary = "保存oss配置")
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
        if ("ALIOSS".equals(oss.getType())) {
            redisTemplate.opsForHash().putAll(RedisConstant.ALIOSS_PIX, BeanUtil.beanToMap(ossDto));
            redisTemplate.expire(RedisConstant.ALIOSS_PIX, 300000000, TimeUnit.DAYS);
        } else if ("TENCENTCOS".equals(oss.getType())) {
            redisTemplate.opsForHash().putAll(RedisConstant.TENCENT_PIX, BeanUtil.beanToMap(ossDto));
            redisTemplate.expire(RedisConstant.TENCENT_PIX, 300000000, TimeUnit.DAYS);
        } else if ("MINIO".equals(oss.getType())) {
            redisTemplate.opsForHash().putAll(RedisConstant.MINIO_PIX, BeanUtil.beanToMap(ossDto));
            redisTemplate.expire(RedisConstant.MINIO_PIX, 300000000, TimeUnit.DAYS);
        }
        return ossService.saveOrUpdate(oss);
    }

    @GetMapping("listFiles/{type}")
    @Operation(summary = "查询分类存储配置")
    public CommonResult<?> listFiles(@PathVariable("type") String type,
                                     @RequestParam("prefix") String prefix,
                                     @RequestParam(value = "nextmarker", required = false) String nextmarker) {
        if ("ALIOSS".equals(type)) {
            return CommonResult.success(OssUtil.getAliOss(redisTemplate).listFiles(50, nextmarker, prefix));
        } else if ("TENCENTCOS".equals(type)) {
            return CommonResult.success(OssUtil.getTencentOss(redisTemplate).listFiles(50, nextmarker, prefix));
        } else if ("MINIO".equals(type)) {
            return CommonResult.success(OssUtil.getMinioOss(redisTemplate).listFiles(50, nextmarker, prefix));
        }
        return CommonResult.success(new ArrayList<>());
    }

    @GetMapping("downloadFile/{type}")
    @Operation(summary = "分类下载文件")
    public void downloadFile(@PathVariable("type") String type,
                             @RequestParam("key") String key, HttpServletResponse response) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        FileDto fIleDto = new FileDto();
        if ("ALIOSS".equals(type)) {
            fIleDto = OssUtil.getAliOss(redisTemplate).downLoad(key);
        } else if ("TENCENTCOS".equals(type)) {
            fIleDto = OssUtil.getTencentOss(redisTemplate).downLoad(key);
        } else if ("MINIO".equals(type)) {
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
    @Operation(summary = "分类下载文件链接")
    public String downloadFileLink(@PathVariable("type") String type,
                                   @RequestParam("key") String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String result = "";
        if ("ALIOSS".equals(type)) {
            result = OssUtil.getAliOss(redisTemplate).downLoadLink(key, 100L);
        } else if ("TENCENTCOS".equals(type)) {
            result = OssUtil.getTencentOss(redisTemplate).downLoadLink(key, 100L);
        } else if ("MINIO".equals(type)) {
            result = OssUtil.getMinioOss(redisTemplate).downLoadLink(key, 100);
        }
        return result;
    }
}
