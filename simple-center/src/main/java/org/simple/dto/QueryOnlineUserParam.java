package org.simple.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * QueryOnlineUserParam
 *
 * @author xiaozhi
 * @version v1.0
 * @since 2022-11-27 19:53:32
 */
@Data
public class QueryOnlineUserParam {

    @NotNull(message = "页数不能为空")
    private Integer size;

    @NotNull(message = "页码不能为空")
    private Integer current;

    private String name;
}
