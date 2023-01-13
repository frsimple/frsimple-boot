package org.simple.dto;

import lombok.Data;

/**
 * SmsDto
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
public class SmsDto {

    private String endpoint;
    private String region;
    private String appid;
    private String secretId;
    private String secretKey;
    private String sign;
}
