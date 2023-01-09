package org.simple.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


/**
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/19
 */
@Data
public class PageModel {

    /**
     * 当前页数
     */
    @Schema(description = "当前页数")
    private int current;

    /**
     * 每页多少条
     */
    @Schema(description = "每页多少条，默认20")
    private int pageSize = 20;

    /**
     * 默认查询
     */
    @Schema(description = "默认查询")
    private String searchValue;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String sortField;

    /**
     * 排序方式
     */
    @Schema(description = "排序方式")
    private String sortOrder;

    /**
     * 总行数
     */
    @Schema(description = "总行数")
    private Long total;

    /**
     * 设置数据列表
     *
     * @param <T>     实体
     * @param list    数据列表
     * @param records 总行数
     * @return 数据列表
     */
    public <T> List<T> setList(List<T> list, long records) {
        this.total = records;
        return list;
    }
}
