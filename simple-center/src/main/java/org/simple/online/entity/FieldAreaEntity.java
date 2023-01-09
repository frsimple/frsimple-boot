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
 * @since 2022/8/7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("online_data_field_area")
@Schema(name = "数据域对象", description = "数据域表")
public class FieldAreaEntity extends BaseEntity {

    /**
     * 数据域代码
     */
    @Schema(description = "数据域代码")
    @TableField(value = "code")
    private String code;

    /**
     * 显示名称
     */
    @Schema(description = "显示名称")
    @TableField(value = "name")
    private String name;

    /**
     * 数据类型
     */
    @Schema(description = "数据类型")
    @TableField(value = "field_type")
    private String fieldType;

    /**
     * 数据类型编号
     */
    @Schema(description = "数据类型编号")
    @TableField(value = "field_code")
    private String fieldCode;

    /**
     * 长度
     */
    @Schema(description = "长度")
    @TableField(value = "field_length")
    private int fieldLength;

    /**
     * 小数位数
     */
    @Schema(description = "小数位数")
    @TableField(value = "field_decimal_length")
    private int fieldDecimalLength;

}
