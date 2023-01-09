package org.simple.online.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.simple.dto.BaseEntity;

/**
 * 系统数据类型表
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-08-06
 */
@Data
@TableName("online_data_type")
@Accessors(chain = true)
@Schema(name = "系统数据类型表")
@EqualsAndHashCode(callSuper = false)
public class DataTypeEntity extends BaseEntity {

    /**
     * 名称
     */
    @Schema(description = "名称")
    @TableField("name")
    private String name;

    /**
     * 编号
     */
    @Schema(description = "编号")
    @TableField("code")
    private String code;

    /**
     * 类型
     */
    @Schema(description = "类型")
    @TableField("type")
    private String type;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @TableField("state")
    private String state;

}
