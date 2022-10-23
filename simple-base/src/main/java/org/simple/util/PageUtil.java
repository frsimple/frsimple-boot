package org.simple.util;

import java.util.List;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022/9/20
 */
public class PageUtil {

    /**
     * @param page     页码
     * @param pageSize 每页行数
     * @param list     数据列表
     * @param <T>      实体
     * @return 自定义分页
     */
    public static <T> List<T> getListPage(Long page, Long pageSize, List<T> list) {
        if (list == null || list.size() == 0) {
            return list;
        }
        long totalCount = list.size();
        page = page - 1;
        long fromIndex = page * pageSize;
        if (fromIndex >= totalCount) {
            return list;
        }
        long toIndex = ((page + 1) * pageSize);
        if (toIndex > totalCount) {
            toIndex = totalCount;
        }
        return list.subList(Math.toIntExact(fromIndex), Math.toIntExact(toIndex));
    }
}
