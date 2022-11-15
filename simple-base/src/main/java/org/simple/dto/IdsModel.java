package org.simple.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/23
 */
@Data
public class IdsModel {

    /**
     * id逗号分隔
     */
    @Schema(description = "id集合")
    private String ids;
}
