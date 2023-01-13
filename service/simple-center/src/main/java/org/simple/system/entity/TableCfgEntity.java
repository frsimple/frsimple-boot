package org.simple.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.BaseEntity;

/**
 * code_tablecfg
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "code_tablecfg")
public class TableCfgEntity extends BaseEntity {

    @Schema(description = "数据源")
    @TableField("datasource")
    private String datasource;

    @Schema(description = "表名")
    @TableField("table_name")
    private String tableName;

    @Schema(description = "修理")
    @TableField("fix")
    private String fix;

    @Schema(description = "软件包")
    @TableField("pkg")
    private String pkg;

    @Schema(description = "认证")
    @TableField("auth")
    private String auth;

}
