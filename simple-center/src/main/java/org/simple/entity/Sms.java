package org.simple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Sms
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@Accessors(chain = true)
@TableName(value = "center_sms", autoResultMap = true)
public class Sms {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String type;
    private String endpoint;
    private String region;
    private String appid;
    private String secretid;
    private String secretkey;
    private String sign;
}
