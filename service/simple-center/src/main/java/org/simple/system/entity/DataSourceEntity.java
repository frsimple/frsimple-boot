package org.simple.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.BaseEntity;

/**
 * DataSource
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "code_datasource")
public class DataSourceEntity extends BaseEntity {

    @Schema(description = "地址")
    @TableField("url")
    private String url;

    @Schema(description = "用户名")
    @TableField("user")
    private String user;

    @Schema(description = "密码")
    @TableField("pwd")
    private String pwd;

    @Schema(description = "名称")
    @TableField("name")
    private String name;
}
