package org.simple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "center_logs")
public class Logs {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
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
