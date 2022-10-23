package org.simple.enums.system;

import java.util.Arrays;

/**
 * 文件存储方式枚举
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/7
 */
public enum StorageModeEnum {

    /**
     * minio
     */
    MINIO("minio", "minio"),

    /**
     * 本地
     */
    LOCAL("local", "本地"),

    /**
     * 阿里云对象存储OSS
     */
    OSS("oss", "阿里云对象存储OSS");

    /**
     * 编号
     */
    private final String key;

    /**
     * 名称
     */
    private final String value;

    StorageModeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 根据枚举key获取枚举
     *
     * @param key 编号
     * @return 枚举
     */
    public static StorageModeEnum fromCode(Integer key) {
        return Arrays.stream(values()).filter(v -> v.getKey().equals(key)).findFirst().orElse(null);
    }

    /**
     * 根据枚举value获取枚举
     *
     * @param value 名称
     * @return 枚举
     */
    public static StorageModeEnum fromDesc(String value) {
        return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst().orElse(null);
    }

    /**
     * 获取枚举key
     *
     * @return 枚举key
     */
    public String getKey() {
        return key;
    }

    /**
     * 获取枚举value
     *
     * @return 枚举value
     */
    public String getValue() {
        return value;
    }
}
