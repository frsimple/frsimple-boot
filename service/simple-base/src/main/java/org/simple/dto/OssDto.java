package org.simple.dto;

import lombok.Data;

/**
 * OssDto
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
public class OssDto {

    private String endpoint;
    private String workspace;
    private String accessKeyId;
    private String accessKeySecret;
    private String appid;
    private String region;
}
