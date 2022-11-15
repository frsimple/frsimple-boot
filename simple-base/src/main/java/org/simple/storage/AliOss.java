package org.simple.storage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import org.simple.constant.RedisConstant;
import org.simple.dto.FIleDto;
import org.simple.dto.OssDto;
import org.simple.utils.ComUtil;
import org.simple.utils.CommonResult;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AliOss {
    private static AliOss aliOss = null;

    private static OssDto ossDto;

    private AliOss() {
    }

    public static AliOss getInstance(RedisTemplate template) {
        if (null == aliOss) {
            aliOss = new AliOss();
        }
        //设置配置对象
        OssDto var = BeanUtil.fillBeanWithMap(
                template.opsForHash().entries(RedisConstant.ALIOSS_PIX), new OssDto(),
                false);
        ossDto = var;
        return aliOss;
    }

    private OSS getOssClient() {
        return new OSSClientBuilder().build(ossDto.getEndpoint(),
                ossDto.getAccesskeyid(), ossDto.getAccesskeysecret());
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
                return CommonResult.success(path);
            } else {
                return CommonResult.success(
                        "https://" + ossDto.getWorkspace() + "." + ossDto.getEndpoint() + "/" + path
                );
            }
        } catch (Exception ex) {
            return CommonResult.failed("上传失败：" + ex.getMessage());
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
    public FIleDto downLoad(String filepath) throws IOException {
        OSS ossClient = getOssClient();
        OSSObject ossObject = ossClient.getObject(ossDto.getWorkspace(), filepath);
        ossClient.shutdown();
        FIleDto f = new FIleDto();
        f.setFileName(ossObject.getKey());
        f.setFileBytes(ComUtil.toByteArray(ossObject.getObjectContent()));
        ossObject.close();
        return f;
    }

    /**
     * 查询文件列表
     */
    public FIleDto listFiles(Integer size, String marker, String prefix) {
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
        FIleDto fIleDto = new FIleDto();
        fIleDto.setFileList(listfile);
        fIleDto.setNextMarker(objectListing.getNextMarker());
        return fIleDto;
    }
}
