package org.simple.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * id逗号分割实体
 *
 * @author yh
 * @version v1.0
 * @since 2022/12/13
 */
@Data
public class IdsModel {
    @Schema(description = "id集合")
    private String id;
}
