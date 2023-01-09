package org.simple.dto;

import lombok.Data;

/**
 * @author ThePai
 * @version v1.0
 * @since 2022/8/2
 */
@Data
public class EnumVO {
    /**
     * 编号
     */
    private String key;

    /**
     * 名称
     */
    private String value;
}
