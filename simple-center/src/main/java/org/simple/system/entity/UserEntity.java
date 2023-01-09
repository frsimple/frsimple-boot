package org.simple.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.BaseEntity;

import java.util.Date;

/**
 * User
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("center_user")
public class UserEntity extends BaseEntity {

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @Schema(description = "昵称")
    @TableField("nick_name")
    private String nickName;

    @Schema(description = "电话")
    @TableField("phone")
    private String phone;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "状态")
    @TableField("status")
    private String status;

    @Schema(description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "登录时间")
    @TableField("login_date")
    private Date loginDate;

    @Schema(description = "错误次数")
    @TableField("error_count")
    private Integer errorCount;

    @Schema(description = "锁定时间")
    @TableField("lock_time")
    private Date lockTime;

    @Schema(description = "关联主机构")
    @TableField("tenant")
    private String tenant;

    @Schema(description = "关联组织")
    @TableField("organ")
    private String organ;
}