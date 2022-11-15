package org.simple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 邮件实体
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/23
 */

@Data
@TableName(value = "center_email")
public class Email {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String host;
    private String port;
    private String username;
    private String password;
    private String sitename;
    private Integer isssl;
}
