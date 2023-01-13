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
@TableName("online_data_table_column")
@Schema(name = "表设计字段对象", description = "表设计字段表")
public class TableColumnEntity extends BaseEntity {

    /**
     * 所属表ID
     */
    @Schema(description = "所属表ID")
    @TableField(value = "table_id")
    private String tableId;

    /**
     * 字段代码
     */
    @Schema(description = "字段代码")
    @TableField(value = "code")
    private String code;

    /**
     * 显示名称
     */
    @Schema(description = "显示名称")
    @TableField(value = "name")
    private String name;

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableField(value = "primary_key")
    private String primaryKey;

    /**
     * 数据域
     */
    @Schema(description = "数据域")
    @TableField(value = "area")
    private String area;

    /**
     * 数据类型
     */
    @Schema(description = "数据类型")
    @TableField(value = "area_type")
    private String areaType;

    /**
     * 长度
     */
    @Schema(description = "长度")
    @TableField(value = "data_length")
    private Double dataLength;

    /**
     * 小数位数
     */
    @Schema(description = "小数位数")
    @TableField(value = "decimal_digits")
    private Double decimalDigits;

    /**
     * 说明
     */
    @Schema(description = "说明")
    @TableField(value = "table_explain")
    private String tableExplain;
}
