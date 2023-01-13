package org.simple.storage;


import org.simple.utils.RedisUtil;

/**
 * 文件存储工具类
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public class OssUtil {

    public static AliOss getAliOss(RedisUtil redisUtil) {
        return AliOss.getInstance(redisUtil);
    }

    public static TencentOss getTencentOss(RedisUtil redisUtil) {
        return TencentOss.getInstance(redisUtil);
    }

    public static MinioOss getMinioOss(RedisUtil redisUtil) {
        return MinioOss.getInstance(redisUtil);
    }
}
