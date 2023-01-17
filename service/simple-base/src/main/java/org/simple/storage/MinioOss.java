package org.simple.storage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.simple.constant.RedisConst;
import org.simple.dto.FileDto;
import org.simple.dto.OssDto;
import org.simple.enums.system.ResultCodeEnum;
import org.simple.utils.ComUtil;
import org.simple.utils.CommonResult;
import org.simple.utils.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * MinioOss
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public class MinioOss {
    private static MinioOss minioOss = null;
    private static OssDto ossDto;

    private RedisTemplate redisTemplate;

    private MinioOss() {
    }

    public static MinioOss getInstance(RedisUtil redisUtil) {
        if (null == minioOss) {
            minioOss = new MinioOss();
        }
        //设置配置对象
        OssDto var = BeanUtil.fillBeanWithMap(
                redisUtil.entries(RedisConst.MINIO_PIX), new OssDto(),
                false);
        ossDto = var;
        return minioOss;
    }

    private MinioClient getMinioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(ossDto.getEndpoint())
                .credentials(ossDto.getAccessKeyId(), ossDto.getAccessKeySecret()).build();
        return minioClient;
    }


    public CommonResult fileUpload(File file, boolean isPrivate, String userid) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = getMinioClient();
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
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(ossDto.getWorkspace())
                .object(path)
                .stream(new FileInputStream(file), file.length(), -1)
                .contentType(new MimetypesFileTypeMap().getContentType(file))
                .build();
        minioClient.putObject(args);

        if (isPrivate) {
            return CommonResult.success(ResultCodeEnum.SUCCESS.getCode(), path);
        } else {
            return CommonResult.success(ResultCodeEnum.SUCCESS.getCode(), ossDto.getEndpoint() + "/" + ossDto.getWorkspace() + "/" + path);
        }
    }


    /**
     * 获取私有文件授权链接
     *
     * @param filepath
     */
    public String downLoadLink(String filepath, Integer expir) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = getMinioClient();
        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(ossDto.getWorkspace())
                .object(filepath)
                .expiry(expir, TimeUnit.SECONDS)
                .build());
        return url;
    }


    /**
     * 下载文件，返回输入流
     *
     * @param filepath
     */
    public FileDto downLoad(String filepath) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = getMinioClient();
        FileDto fIleDto = new FileDto();
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(ossDto.getWorkspace())
                        .object(filepath)
                        .build())) {
            fIleDto.setFileName(filepath);
            fIleDto.setFileBytes(ComUtil.toByteArray(stream));
        }
        return fIleDto;
    }


    /**
     * 查询文件列表
     */
    public FileDto listFiles(Integer size, String marker, String prefix) {
        MinioClient minioClient = getMinioClient();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(ossDto.getWorkspace())
                        .startAfter(marker)
                        .prefix(prefix)
                        .maxKeys(size)
                        .build());
        List<org.simple.dto.File> listfile = new ArrayList<>();
        results.forEach(e -> {
            try {
                String childPath = e.get().objectName();
                org.simple.dto.File f = new org.simple.dto.File();
                f.setKey(childPath);
                f.setSize(e.get().size());
                if (!e.get().isDir()) {
                    f.setUpdateDate(Date.from(e.get().lastModified().toInstant()));
                }
                listfile.add(f);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        FileDto fIleDto = new FileDto();
        fIleDto.setFileList(listfile);
        return fIleDto;
    }
}
