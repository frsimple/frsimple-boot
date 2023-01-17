package org.simple.storage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.simple.constant.RedisConst;
import org.simple.dto.FileDto;
import org.simple.dto.OssDto;
import org.simple.enums.system.ResultCodeEnum;
import org.simple.utils.ComUtil;
import org.simple.utils.CommonResult;
import org.simple.utils.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TencentOss
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public class TencentOss {

    private static TencentOss tencentOss = null;
    private static OssDto ossDto;
    private RedisTemplate redisTemplate;

    private TencentOss() {
    }

    public static TencentOss getInstance(RedisUtil redisUtil) {
        if (null == tencentOss) {
            tencentOss = new TencentOss();
        }
        //设置配置对象
        OssDto var = BeanUtil.fillBeanWithMap(
                redisUtil.entries(RedisConst.TENCENT_PIX), new OssDto(),
                false);
        ossDto = var;
        return tencentOss;
    }

    private COSClient getCosClient() {
        COSCredentials cred = new BasicCOSCredentials(ossDto.getAccessKeyId(),
                ossDto.getAccessKeySecret());
        Region region = new Region(ossDto.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(cred, clientConfig);
    }

    public CommonResult fileUpload(File file, boolean isPrivate, String userid) {
        //初始化ossclient对象
        COSClient cosClient = getCosClient();
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
                putObjectRequest.setCannedAcl(CannedAccessControlList.Private);
            }
            cosClient.putObject(putObjectRequest);
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
            cosClient.shutdown();
        }
    }

    /**
     * 获取私有文件授权链接
     *
     * @param filepath
     */
    public String downLoadLink(String filepath, Long expir) {
        COSClient cosClient = getCosClient();
        Date expiration = new Date(System.currentTimeMillis() + expir * 1000);
        URL url = cosClient.generatePresignedUrl(ossDto.getWorkspace(), filepath, expiration);
        cosClient.shutdown();
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
        COSClient cosClient = getCosClient();
        GetObjectRequest getObjectRequest = new GetObjectRequest(ossDto.getWorkspace(), filepath);
        COSObject cosObject = cosClient.getObject(getObjectRequest);
        COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
        cosClient.shutdown();
        FileDto fIleDto = new FileDto();
        fIleDto.setFileName(cosObject.getKey());
        fIleDto.setFileBytes(ComUtil.toByteArray(cosObjectInput));
        cosObject.close();
        return fIleDto;
    }


    /**
     * 查询文件列表
     */
    public FileDto listFiles(Integer size, String marker, String prefix) {
        COSClient cosClient = getCosClient();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(ossDto.getWorkspace());
        listObjectsRequest.setPrefix(prefix);
        listObjectsRequest.setMaxKeys(size);
        ObjectListing objectListing = null;
        try {
            objectListing = cosClient.listObjects(listObjectsRequest);
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        }
        List<COSObjectSummary> list = objectListing.getObjectSummaries();
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
