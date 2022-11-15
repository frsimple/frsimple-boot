package org.simple.dto;

import lombok.Data;

import java.time.LocalDateTime;


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
