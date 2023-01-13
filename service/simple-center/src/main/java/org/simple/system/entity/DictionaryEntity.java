package org.simple.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.BaseEntity;


/**
 * 数据字典实体
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "center_dictionary")
public class DictionaryEntity extends BaseEntity {

    @Schema(description = "编号")
    @TableField("code")
    private String code;

    @Schema(description = "值")
    @TableField("value")
    private String value;

    @Schema(description = "文本")
    @TableField("label")
    private String label;
}
