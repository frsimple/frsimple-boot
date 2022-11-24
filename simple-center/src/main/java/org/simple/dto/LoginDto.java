package org.simple.dto;

import lombok.Builder;
import lombok.Data;

/**
 * LogsDto
 *
 * @author xiaozhi
 * @version v1.0
 * @since 2022-11-24 21:50:42
 */
@Data
@Builder
public class LoginDto {

    // 登录后的token值
    private String token;
}
