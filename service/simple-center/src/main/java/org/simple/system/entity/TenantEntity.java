package org.simple.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.simple.dto.BaseEntity;

/**
 * Tenant
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "center_tenant", autoResultMap = true)
public class TenantEntity extends BaseEntity {

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "电话")
    @TableField("phone")
    private String phone;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "地址")
    @TableField("address")
    private String address;

    @Schema(description = "状态")
    @TableField("status")
    private String status;

}
