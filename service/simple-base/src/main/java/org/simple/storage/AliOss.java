package org.simple.storage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import org.simple.constant.RedisConst;
import org.simple.dto.FileDto;
import org.simple.dto.OssDto;
import org.simple.enums.system.ResultCodeEnum;
import org.simple.utils.ComUtil;
import org.simple.utils.CommonResult;
import org.simple.utils.RedisUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Oss
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

public class AliOss {
    private static AliOss aliOss = null;

    private static OssDto ossDto;

    private AliOss() {
    }

    public static AliOss getInstance(RedisUtil redisUtil) {
        if (null == aliOss) {
            aliOss = new AliOss();
        }
        //设置配置对象
        OssDto var = BeanUtil.fillBeanWithMap(
                redisUtil.entries(RedisConst.ALIOSS_PIX), new OssDto(),
                false);
        ossDto = var;
        return aliOss;
    }

    private OSS getOssClient() {
        return new OSSClientBuilder().build(ossDto.getEndpoint(),
                ossDto.getAccessKeyId(), ossDto.getAccessKeySecret());
    }

    public CommonResult fileUpload(File file, boolean isPrivate, String userid) {
        //初始化ossclient对象
        OSS ossClient = getOssClient();
        String fileName = file.getName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String path = "";
        if (!isPrivate) {
            path = "public/";
        } else {
            path = "private/";
        }
        path = path + sdf.format(new Date()) + "/";
        if (StrUtil.isNotEmpty(userid)) {
            path = path + userid + "/" + fileName;
        }
        // 创建PutObjectRequest对象。
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossDto.getWorkspace(), path, file);
            // 如果需要上传时设置存储类型与访问权限
            if (isPrivate) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
                metadata.setObjectAcl(CannedAccessControlList.Private);
                putObjectRequest.setMetadata(metadata);
            }
            ossClient.putObject(putObjectRequest);
            //若是私有连接则返回上传路径，若是公共读则返回请求的url地址
            if (isPrivate) {
                return CommonResult.success(ResultCodeEnum.SUCCESS.getCode(), path);
            } else {
                return CommonResult.success(ResultCodeEnum.SUCCESS.getCode(),
                        "https://" + ossDto.getWorkspace() + "." + ossDto.getEndpoint() + "/" + path
                );
            }
        } catch (Exception ex) {
            return CommonResult.failed(ex.getMessage());
        } finally {
            //最后关闭ossclient
            ossClient.shutdown();
        }
    }

    /**
     * 获取私有文件授权链接
     *
     * @param filepath
     */
    public String downLoadLink(String filepath, Long expir) {
        OSS ossClient = getOssClient();
        Date expiration = new Date(System.currentTimeMillis() + expir * 1000);
        URL url = ossClient.generatePresignedUrl(ossDto.getWorkspace(), filepath, expiration);
        ossClient.shutdown();
        if (url.toString().indexOf("https") == -1) {
            return url.toString().replace("http", "https");
        } else {
            return url.toString();
        }
    }


    /**
     * 下载文件，返回输入流
     *
     * @param filepath
     */
    public FileDto downLoad(String filepath) throws IOException {
        OSS ossClient = getOssClient();
        OSSObject ossObject = ossClient.getObject(ossDto.getWorkspace(), filepath);
        ossClient.shutdown();
        FileDto f = new FileDto();
        f.setFileName(ossObject.getKey());
        f.setFileBytes(ComUtil.toByteArray(ossObject.getObjectContent()));
        ossObject.close();
        return f;
    }

    /**
     * 查询文件列表
     */
    public FileDto listFiles(Integer size, String marker, String prefix) {
        OSS ossClient = getOssClient();
        ListObjectsRequest request = new ListObjectsRequest(ossDto.getWorkspace());
        if (StrUtil.isNotEmpty(marker)) {
            request.setMarker(marker);
        }
        request.setPrefix(prefix);
        request.setMaxKeys(size);
        ObjectListing objectListing = ossClient.listObjects(request);
        List<OSSObjectSummary> list = objectListing.getObjectSummaries();
        List<org.simple.dto.File> listfile = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(row -> {
                org.simple.dto.File f = new org.simple.dto.File();
                f.setKey(row.getKey());
                f.setSize(row.getSize());
                f.setUpdateDate(row.getLastModified());
                listfile.add(f);
            });
        }
        FileDto fIleDto = new FileDto();
        fIleDto.setFileList(listfile);
        fIleDto.setNextMarker(objectListing.getNextMarker());
        return fIleDto;
    }
}
