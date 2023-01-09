package org.simple.system.dto.table;

import lombok.Data;

import java.util.Date;

/**
 * TableInfo
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
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
