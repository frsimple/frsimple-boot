package org.simple.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 默认字段对象
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/7
 */
@Data
public class BaseEntity {

    /**
     * 主键id
     */
    @Schema(description = "主键id")
    @TableId(value = "id")
    private String id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 创建人id
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    @Schema(description = "创建人id")
    private String createUserId;

    /**
     * 创建人
     */
    @TableField(value = "create_user_name", fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private String createUserName;

    /**
     * 创建人组织id
     */
    @TableField(value = "create_org_id", fill = FieldFill.INSERT)
    @Schema(description = "创建人组织id")
    private String createOrgId;

    /**
     * 修改时间
     */
    @TableField(value = "modify_time", fill = FieldFill.UPDATE)
    @Schema(description = "修改时间")
    private Date modifyTime;

    /**
     * 修改人id
     */
    @TableField(value = "modify_user_id", fill = FieldFill.UPDATE)
    @Schema(description = "修改人id")
    private String modifyUserId;

    /**
     * 修改人
     */
    @TableField(value = "modify_user_name", fill = FieldFill.UPDATE)
    @Schema(description = "修改人")
    private String modifyUserName;

    /**
     * 说明
     */
    @TableField(value = "description")
    @Schema(description = "说明")
    private String description;

    /**
     * 排序号
     */
    @TableField(value = "sort_index")
    @Schema(description = "排序号")
    private Double sortIndex;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    @Schema(description = "租户id")
    private String tenantId;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @Schema(description = "是否删除：true删除，false未删除")
    private String isDeleted;
}
