package org.simple.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Logs
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
public class Logs {
    private static final long serialVersionUID = 1L;
    private String id;
    private String servicename;
    private String recource;
    private String ip;
    private String path;
    private String method;
    private String useragent;
    private String params;
    private String username;

    private LocalDateTime createtime;
    private String time;
    private String status;
    private String error;
}
