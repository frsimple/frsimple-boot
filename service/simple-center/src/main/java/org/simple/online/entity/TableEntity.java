package org.simple.online.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.simple.dto.BaseEntity;

/**
 * 数据域主表
 *
 * @author ThePai
 * @version v1.0
 * @since 2022/8/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("online_data_table")
@Schema(name = "表设计对象", description = "表设计表")
public class TableEntity extends BaseEntity {

    /**
     * 库名
     */
    @Schema(description = "库名")
    @TableField(value = "db_name")
    private String dbName;

    /**
     * 编号
     */
    @Schema(description = "编号")
    @TableField(value = "code")
    private String code;

    /**
     * 表名
     */
    @Schema(description = "表名")
    @TableField(value = "table_Code")
    private String tableCode;

    /**
     * 显示名称
     */
    @Schema(description = "显示名称")
    @TableField(value = "table_name")
    private String tableName;
}
