package org.simple.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.BaseEntity;


/**
 * 组织实体
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/12/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "center_organ")
public class OrganEntity extends BaseEntity {

    /**
     * 节点名称
     */
    @Schema(description = "名称")
    @TableField("name")
    private String name;

    /**
     * 父节点id
     */
    @Schema(description = "父节点id")
    @TableField("parent_id")
    private Long parentId;

    /**
     * 父节点id
     */
    @Schema(description = "排序")
    @TableField("sort")
    private Long sort;
}
