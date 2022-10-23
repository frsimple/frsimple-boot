package org.simple.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author ThePai
 * @version v1.0
 * @since 2022/0805/23
 */
@Data
public class CodesModel {

    /**
     * code集合
     */
    @Schema(description = "code集合")
    private String codes;
}
