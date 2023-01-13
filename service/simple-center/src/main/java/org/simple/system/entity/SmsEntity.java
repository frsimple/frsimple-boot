package org.simple.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.simple.dto.BaseEntity;

/**
 * Sms
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "center_sms", autoResultMap = true)
public class SmsEntity extends BaseEntity {

    @Schema(description = "类型")
    @TableField("type")
    private String type;

    @Schema(description = "输出地址")
    @TableField("endpoint")
    private String endpoint;

    @Schema(description = "区域")
    @TableField("region")
    private String region;

    @Schema(description = "appid")
    @TableField("appid")
    private String appid;

    @Schema(description = "密钥id")
    @TableField("secret_id")
    private String secretId;

    @Schema(description = "密钥key")
    @TableField("secret_key")
    private String secretKey;

    @Schema(description = "签名")
    @TableField("sign")
    private String sign;
}
