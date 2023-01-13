package org.simple.online.dto.fieldarea;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.BaseEntity;

import java.util.List;

/**
 * 系统数据域表
 *
 * @author ThePai
 * @version v1.0
 * @since 2022-8-7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FieldAreaFormVO extends BaseEntity {

    /**
     * 数据域代码
     */
    @Schema(description = "数据域代码")
    private String code;

    /**
     * 显示名称
     */
    @Schema(description = "显示名称")
    private String name;

    /**
     * 数据类型
     */
    @Schema(description = "数据类型")
    private String fieldType;

    /**
     * 数据类型编号
     */
    @Schema(description = "数据类型编号")
    private String fieldCode;

    /**
     * 长度
     */
    @Schema(description = "长度")
    private Long fieldLength;

    /**
     * 小数位数
     */
    @Schema(description = "小数位数")
    private Long fieldDecimalLength;

    /**
     * 明细信息
     */
    @Schema(description = "明细信息")
    private List<FieldAreaDetailFormVO> detail;
}
