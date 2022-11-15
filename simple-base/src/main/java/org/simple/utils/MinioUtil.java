package org.simple.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import org.simple.config.minio.MinioProperties;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/12
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUtil {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    /**
     * 创建bucket
     *
     * @param bucketName 存储桶
     */
    public void createBucket(String bucketName) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param bucketName 存储桶
     * @param objectName 对象
     * @return true：存在
     */
    public boolean objectExist(String bucketName, String objectName) {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    /**
     * 判断文件夹是否存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件夹名称（去掉/）
     * @return true：存在
     */
    public boolean folderExist(String bucketName, String objectName) {
        boolean exist = false;
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).prefix(objectName).recursive(false).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.isDir() && objectName.equals(item.objectName())) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }


    /**
     * 通过MultipartFile，上传文件
     *
     * @param bucketName 存储桶
     * @param file       文件
     * @param objectName 对象名
     */
    public ObjectWriteResponse putObject(String bucketName, MultipartFile file, String objectName, String contentType)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        InputStream inputStream = file.getInputStream();
        return minioClient.putObject(PutObjectArgs
                .builder()
                .bucket(bucketName)
                .object(objectName)
                .contentType(contentType)
                .stream(inputStream, inputStream.available(), -1)
                .build());
    }

    /**
     * 上传本地文件
     *
     * @param bucketName 存储桶
     * @param objectName 对象名称
     * @param fileName   本地文件路径
     */
    public ObjectWriteResponse putObject(String bucketName, String objectName, String fileName)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        return minioClient.uploadObject(UploadObjectArgs
                .builder()
                .bucket(bucketName)
                .object(objectName)
                .filename(fileName)
                .build());
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶
     * @param objectName ⽂件名称
     * @param stream     ⽂件流
     * @throws Exception 异常信息
     */
    public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
        putObjectByStream(bucketName, objectName, stream);
    }

    /**
     * 通过流上传文件
     *
     * @param bucketName  存储桶
     * @param objectName  文件对象
     * @param inputStream 文件流
     */
    public ObjectWriteResponse putObjectByStream(String bucketName, String objectName, InputStream inputStream)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        return minioClient.putObject(PutObjectArgs
                .builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(inputStream, inputStream.available(), -1)
                .build());
    }

    /**
     * 创建文件夹或目录
     *
     * @param bucketName 存储桶
     * @param objectName 目录路径
     */
    public ObjectWriteResponse putDirObject(String bucketName, String objectName)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        return minioClient.putObject(PutObjectArgs
                .builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                .build());
    }


    /**
     * 上传文件
     *
     * @param file       文件
     * @param bucketName 存储桶
     **/
    public JSONObject uploadFile(MultipartFile file, String bucketName) throws Exception {
        JSONObject res = new JSONObject();
        res.putOnce("code", 0);
        //判断文件是否为空
        if (null == file || 0 == file.getSize()) {
            res.putOnce("msg", "上传文件不能为空");
            return res;
        }
        //判断存储桶是否存在  不存在则创建
        createBucket(bucketName);
        //文件名
        String originalFilename = file.getOriginalFilename();
        //新的文件名 = 存储桶文件名_时间戳.后缀名
        String fileName = null;
        if (originalFilename != null) {
            fileName = bucketName + "_" +
                    System.currentTimeMillis() +
                    originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        //开始上传
        putObjectByStream(bucketName, fileName, file.getInputStream());
        res.putOnce("code", 1);
        res.putOnce("fileName", fileName);
        res.putOnce("msg", minioProperties.getEndpoint() + "/" + bucketName + "/" + fileName);
        return res;
    }

    /**
     * 上传文件
     *
     * @param filepath   文件路径，
     * @param bucketName 存储桶
     * @return json格式文件信息
     */
    public JSONObject uploadFile(String filepath, String bucketName) throws Exception {
        File oldFile = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(oldFile);
        JSONObject res = new JSONObject();
        res.putOnce("code", 0);
        //判断文件是否为空
        if (FileUtil.isEmpty(oldFile)) {
            res.putOnce("msg", "上传文件不能为空");
            return res;
        }
        //判断存储桶是否存在  不存在则创建
        createBucket(bucketName);
        //文件名
        String originalFilename = oldFile.getName();
        //新的文件名 = 存储桶文件名_时间戳_UUID.后缀名
        String fileName = bucketName + "_" +
                System.currentTimeMillis() +
                originalFilename.substring(originalFilename.lastIndexOf("."));
        //开始上传
        putObjectByStream(bucketName, fileName, fileInputStream);
        res.putOnce("code", 1);
        res.putOnce("fileName", fileName);
        res.putOnce("msg", minioProperties.getEndpoint() + "/" + bucketName + "/" + fileName);
        return res;
    }

    /**
     * 获取全部存储桶
     *
     * @return 所有存储桶
     */
    public List<Bucket> getAllBuckets() throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * 获取文件地址
     *
     * @param bucketName 存储桶
     * @param objectName ⽂件名称
     * @param expires    过期时间
     * @return url
     */
    public String getObjectUrl(String bucketName, String objectName, Integer expires) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs
                        .builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expires)
                        .build());
    }

    /**
     * 获取文件
     *
     * @param bucketName 存储桶
     * @param objectName ⽂件名称
     * @return ⼆进制流
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs
                        .builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    /**
     * 获取文件信息
     *
     * @param bucketName 存储桶
     * @param objectName ⽂件名称
     * @throws Exception 异常信息
     */
    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
        return minioClient.statObject(
                StatObjectArgs
                        .builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶
     * @throws 异常信息
     */
    public void removeBucket(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        minioClient.removeBucket(
                RemoveBucketArgs
                        .builder()
                        .bucket(bucketName)
                        .build());
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶
     * @param objectName ⽂件名称
     * @throws Exception 异常信息
     */
    public void removeObject(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs
                        .builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }
}