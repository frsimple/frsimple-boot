package org.simple.online.dto.table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.BaseEntity;

/**
 * 数据表定义列明细表
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-09-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TableColumnVO extends BaseEntity {

    /**
     * 所属表ID
     */
    @Schema(description = "所属表ID")
    private String tableId;

    /**
     * 字段代码
     */
    @Schema(description = "字段代码")
    private String code;

    /**
     * 显示名称
     */
    @Schema(description = "显示名称")
    private String name;

    /**
     * 主键
     */
    @Schema(description = "主键")
    private String primaryKey;

    /**
     * 数据域
     */
    @Schema(description = "数据域")
    private String area;

    /**
     * 数据类型
     */
    @Schema(description = "数据类型")
    private String areaType;

    /**
     * 长度
     */
    @Schema(description = "长度")
    private Integer length;

    /**
     * 小数位数
     */
    @Schema(description = "小数位数")
    private Integer decimalDigits;

    /**
     * 说明
     */
    @Schema(description = "说明")
    private String explain;
}
