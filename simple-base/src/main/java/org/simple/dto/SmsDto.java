package org.simple.dto;

import lombok.Data;

@Data
public class SmsDto {

    private String endpoint;
    private String region;
    private String appid;
    private String secretid;
    private String secretkey;
    private String sign;
}
