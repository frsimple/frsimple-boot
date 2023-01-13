package org.simple.online.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.simple.dto.BaseEntity;

/**
 * 数据域子表
 *
 * @author ThePai
 * @version v1.0
 * @since 2022/8/7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("online_data_field_area_detail")
@Schema(name = "数据域对象", description = "数据域子表")
public class FieldAreaDetailEntity extends BaseEntity {

    /**
     * 所属ID
     */
    @Schema(description = "所属ID")
    @TableField(value = "field_id")
    private String fieldId;

    /**
     * 数据库类型名称
     */
    @Schema(description = "数据库类型名称")
    @TableField(value = "db_name")
    private String dbName;

    /**
     * 数据域代码
     */
    @Schema(description = "数据域代码")
    @TableField(value = "field_code")
    private String fieldCode;

}
