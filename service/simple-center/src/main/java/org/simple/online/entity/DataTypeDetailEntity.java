package org.simple.online.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.simple.dto.BaseEntity;

/**
 * 系统数据类型明细表
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-08-06
 */
@Data
@TableName("online_data_type_detail")
@Accessors(chain = true)
@Schema(name = "系统数据类型明细表")
@EqualsAndHashCode(callSuper = false)
public class DataTypeDetailEntity extends BaseEntity {

    /**
     * 主表id
     */
    @Schema(description = "主表id")
    @TableField("detail_id")
    private String detailId;

    /**
     * 数据库名称
     */
    @Schema(description = "数据库名称")
    @TableField("name")
    private String name;

    /**
     * 字段类型名称
     */
    @Schema(description = "字段类型名称")
    @TableField("code")
    private String code;

}
