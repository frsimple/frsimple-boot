package org.simple.online.dto.datatype;

import org.simple.dto.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统数据类型明细表
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataTypeDetailVO extends BaseEntity {

    /**
     * 主表id
     */
    @Schema(description = "主表id")
    private String detailId;

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
}
