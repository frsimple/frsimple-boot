package org.simple.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.simple.dto.BaseEntity;

/**
 * Role
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "center_role", autoResultMap = true)
public class RoleEntity extends BaseEntity {
    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "状态")
    @TableField("status")
    private String status;

    @Schema(description = "编号")
    @TableField("code")
    private String code;

    @Schema(description = "类型")
    @TableField("type")
    private String type;
}
