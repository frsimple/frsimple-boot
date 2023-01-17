package org.simple.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yitter.idgen.YitIdHelper;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.constant.CommonConst;
import org.simple.constant.RedisConst;
import org.simple.dto.FileDto;
import org.simple.dto.OssDto;
import org.simple.system.entity.OssEntity;
import org.simple.system.service.IOssService;
import org.simple.storage.OssUtil;
import org.simple.utils.CommonResult;
import org.simple.utils.RedisUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * oss管理
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/system/oss")
@Tag(name = "oss", description = "oss管理")
public class OssController {

    private final RedisUtil redisUtil;
    private final IOssService ossService;

    @GetMapping("{type}")
    @Operation(summary = "获取oss配置")
    public OssEntity getOss(@PathVariable("type") String type) {
        OssEntity o = new OssEntity();
        o.setType(type);
        return ossService.getOne(Wrappers.query(o));
    }

    @PostMapping("saveOrUpdate")
    @Operation(summary = "保存oss配置")
    public Boolean saveOrUpdate(@RequestBody OssEntity ossEntity) {
        OssEntity query = new OssEntity();
        query.setType(ossEntity.getType());
        query = ossService.getOne(Wrappers.query(query));
        if (null != query) {
            ossEntity.setId(query.getId());
        } else {
            ossEntity.setId(String.valueOf(YitIdHelper.nextId()));
        }
        ossService.saveOrUpdate(ossEntity);
        //刷新缓存
        OssDto ossDto = new OssDto();
        ossDto.setAccessKeySecret(ossEntity.getAccessKeySecret());
        ossDto.setAccessKeyId(ossEntity.getAccessKeyId());
        ossDto.setAppid(ossEntity.getAppid());
        ossDto.setRegion(ossEntity.getRegion());
        ossDto.setEndpoint(ossEntity.getEndpoint());
        ossDto.setWorkspace(ossEntity.getWorkspace());
        if (CommonConst.MINIO.equals(ossEntity.getType())) {
            redisUtil.add(RedisConst.MINIO_PIX, BeanUtil.beanToMap(ossDto));
            redisUtil.expire(RedisConst.MINIO_PIX, 300000000, TimeUnit.DAYS);
        }
        return ossService.saveOrUpdate(ossEntity);
    }

    @GetMapping("listFiles/{type}")
    @Operation(summary = "查询分类存储配置")
    public CommonResult<?> listFiles(@PathVariable("type") String type,
                                     @RequestParam("prefix") String prefix,
                                     @RequestParam(value = "nextmarker", required = false) String nextmarker) {
        if (CommonConst.MINIO.equals(type)) {
            return CommonResult.success(OssUtil.getMinioOss(redisUtil).listFiles(50, nextmarker, prefix));
        }
        return CommonResult.success(new ArrayList<>());
    }

    @GetMapping("downloadFile/{type}")
    @Operation(summary = "分类下载文件")
    public void downloadFile(@PathVariable("type") String type,
                             @RequestParam("key") String key, HttpServletResponse response) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        FileDto fIleDto = new FileDto();
        if (CommonConst.MINIO.equals(type)) {
            fIleDto = OssUtil.getMinioOss(redisUtil).downLoad(key);
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
        if (CommonConst.MINIO.equals(type)) {
            result = OssUtil.getMinioOss(redisUtil).downLoadLink(key, 100);
        }
        return result;
    }
}
