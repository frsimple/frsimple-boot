package org.simple.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/16
 */
@Data
public class UserInfo {

    private static final long serialVersionUID = 30649287;

    /**
     * 主键id
     */
    @Schema(description = "主键id")
    @TableField(value = "id")
    private Long userId;

    /**
     * 账号
     */
    @Schema(description = "账号")
    @TableField(value = "user_name")
    private String userName;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    @TableField(value = "real_name")
    private String realName;

    /**
     * 公司id
     */
    @Schema(description = "公司id")
    @TableField(value = "company_id")
    private Long companyId;


    /**
     * 公司名称
     */
    @Schema(description = "公司名称")
    @TableField(value = "company_name")
    private String companyName;

    /**
     * 组织id
     */
    @Schema(description = "组织id")
    @TableField(value = "org_id")
    private Long orgId;

    /**
     * 组织名称
     */
    @Schema(description = "组织名称")
    @TableField(value = "org_name")
    private String orgName;

    /**
     * 最后一次登录ip
     */
    @Schema(description = "最后一次登录ip")
    @TableField(value = "last_login_ip")
    private String lastLoginIp;

    /**
     * 最后一次登录时间
     */
    @Schema(description = "最后一次登录时间")
    @TableField(value = "last_login_time")
    private Date lastLoginTime;

    /**
     * 最后一次登录设备
     */
    @Schema(description = "最后一次登录设备")
    @TableField(value = "last_login_plat_form")
    private String lastLoginPlatForm;

    /**
     * 锁定时间
     */
    @Schema(description = "锁定时间")
    @TableField(value = "lock_time")
    private Date lockTime;

    /**
     * 密码错误次数
     */
    @Schema(description = "密码错误次数")
    @TableField(value = "error_count")
    private Integer errorCount;

    /**
     * 修改密码时间
     */
    @Schema(description = "修改密码时间")
    @TableField(value = "update_pwd_time")
    private Date updatePwdTime;

    /**
     * 用户token
     */
    @Schema(description = "用户token")
    @TableField(value = "token")
    private String token;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @TableField(value = "state")
    private String state;
}
