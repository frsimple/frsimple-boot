package org.simple.online.dto.table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.BaseEntity;

import java.util.List;

/**
 * 表设计对象
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-09-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TableVO extends BaseEntity {

    /**
     * 库名
     */
    @Schema(description = "库名")
    private String dbName;

    /**
     * 编号
     */
    @Schema(description = "编号")
    private String code;

    /**
     * 表名
     */
    @Schema(description = "表名")
    private String tableCode;

    /**
     * 显示名称
     */
    @Schema(description = "显示名称")
    private String tableName;

    /**
     * 明细
     */
    @Schema(description = "明细")
    private List<TableColumnVO> column;
}
