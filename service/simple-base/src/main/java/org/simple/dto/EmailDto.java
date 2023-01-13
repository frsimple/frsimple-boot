package org.simple.dto;

import lombok.Data;

/**
 * EmailDto
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
public class EmailDto {

    private String host;
    private String port;
    private String username;
    private String password;
    private String siteName;
    private Integer isSsl;
}
