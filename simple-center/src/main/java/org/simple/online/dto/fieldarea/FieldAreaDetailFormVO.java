package org.simple.online.dto.fieldarea;

import org.simple.dto.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统数据类型明细表
 *
 * @author ThePai
 * @version v1.0
 * @since 2022-8-7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FieldAreaDetailFormVO extends BaseEntity {

    /**
     * 主表id
     */
    @Schema(description = "主表id")
    private String detailId;

    /**
     * 数据库类型
     */
    @Schema(description = "数据库类型")
    private String dbName;

    /**
     * 数据域代码
     */
    @Schema(description = "数据域代码")
    private String fieldCode;
}
