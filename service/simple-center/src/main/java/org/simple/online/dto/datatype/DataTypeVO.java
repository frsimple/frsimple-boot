package org.simple.online.dto.datatype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.BaseEntity;

import java.util.List;

/**
 * 系统数据类型表
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataTypeVO extends BaseEntity {

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 编号
     */
    @Schema(description = "编号")
    private String code;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private String type;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private String state;

    /**
     * 明细
     */
    @Schema(description = "明细")
    private List<DataTypeDetailVO> detail;
}
