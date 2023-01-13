package org.simple.utils;

import java.util.List;

/**
 * 枚举公用类
 * 因为枚举项的维护及前端页面没写好，所以这里待完善
 *
 * @author ThePai
 * @version v0.1
 * @since 2022/7/25
 */
public class EnumUtil {
    /**
     * 根据枚举编号获取枚举项
     *
     * @param enumCode 枚举编号
     * @return 枚举信息
     */
    public List<?> getEnumList(String enumCode) {
        return null;
    }

    /**
     * 根据枚举编号获取枚举项（批量）
     *
     * @param enumCodes 枚举编号
     * @return 枚举信息
     */
    public List<?> getEnumListBatch(String[] enumCodes) {
        for (String enumCode : enumCodes) {
            getEnumList(enumCode);
        }
        return null;
    }
}
