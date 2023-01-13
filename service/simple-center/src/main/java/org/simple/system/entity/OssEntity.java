package org.simple.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.simple.dto.BaseEntity;

/**
 * Oss
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "center_oss", autoResultMap = true)
public class OssEntity extends BaseEntity {

    @Schema(description = "输出地址")
    @TableField("endpoint")
    private String endpoint;

    @Schema(description = "工作空间")
    @TableField("workspace")
    private String workspace;

    @Schema(description = "keyId")
    @TableField("access_key_id")
    private String accessKeyId;

    @Schema(description = "key密钥")
    @TableField("access_key_secret")
    private String accessKeySecret;

    @Schema(description = "appid")
    @TableField("appid")
    private String appid;

    @Schema(description = "区域")
    @TableField("region")
    private String region;

    @Schema(description = "类型")
    @TableField("type")
    private String type;
}
