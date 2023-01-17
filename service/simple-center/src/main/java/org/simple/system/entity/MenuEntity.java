package org.simple.system.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Menu
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "center_menu", autoResultMap = true)
public class MenuEntity {

    /**
     * 主键id
     */
    @Schema(description = "主键id")
    @TableId(value = "id")
    private String id;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "类型")
    @TableField("type")
    private String type;

    @Schema(description = "权限编号")
    @TableField("authcode")
    private String authCode;

    @Schema(description = "路径")
    @TableField("path")
    private String path;

    @Schema(description = "父节点id")
    @TableField("parentid")
    private String parentId;

    @Schema(description = "组件")
    @TableField("component")
    private String component;

    @Schema(description = "状态")
    @TableField("status")
    private String status;

    @Schema(description = "排序")
    @TableField("sort")
    private String sort;

    @Schema(description = "明细")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONObject meta;
}
