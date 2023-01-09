package org.simple.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


/**
 * Logs
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@TableName(value = "center_logs")
public class LogsEntity {

    @Schema(description = "主键id")
    @TableId(value = "id")
    private String id;

    @TableField(value = "create_time")
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(value = "create_user_id")
    @Schema(description = "创建人id")
    private String createUserId;

    @TableField(value = "create_user_name")
    @Schema(description = "创建人")
    private String createUserName;

    @TableField(value = "create_org_id")
    @Schema(description = "创建人组织id")
    private String createOrgId;

    @TableField(value = "tenant_id")
    @Schema(description = "租户id")
    private String tenantId;

    @TableField(value = "is_deleted")
    @Schema(description = "是否删除：true删除，false未删除")
    private String isDeleted;

    @Schema(description = "登陆用户")
    @TableField("nick_name")
    private String nickName;

    @Schema(description = "主机头")
    @TableField("service_name")
    private String serviceName;

    @Schema(description = "资源")
    @TableField("resource")
    private String resource;

    @Schema(description = "ip地址")
    @TableField("ip")
    private String ip;

    @Schema(description = "路径")
    @TableField("path")
    private String path;

    @Schema(description = "方法")
    @TableField("method")
    private String method;

    @Schema(description = "请求agent")
    @TableField("useragent")
    private String useragent;

    @Schema(description = "参数")
    @TableField("params")
    private String params;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "耗时")
    @TableField("time")
    private String time;

    @Schema(description = "状态")
    @TableField("status")
    private String status;

    @Schema(description = "异常")
    @TableField("error")
    private String error;
}
