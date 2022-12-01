package org.simple.dto;


import lombok.Data;

import java.util.Date;

@Data
public class TableInfo {

    private String tableComment;
    private String tableCollation;
    private Date createTime;
    private String tableName;
    private String fix;
    private String pkg;
    private String auth;
    private String id;
}
