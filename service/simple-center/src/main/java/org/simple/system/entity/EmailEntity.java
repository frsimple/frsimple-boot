package org.simple.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.BaseEntity;

/**
 * 邮件实体
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "center_email")
public class EmailEntity extends BaseEntity {

    @Schema(description = "主机头")
    @TableField("host")
    private String host;

    @Schema(description = "端口")
    @TableField("port")
    private String port;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @Schema(description = "网站名称")
    @TableField("site_name")
    private String siteName;

    @Schema(description = "是否ssl")
    @TableField("is_ssl")
    private Integer isSsl;
}
