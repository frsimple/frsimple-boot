package org.simple.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName(value = "center_oss",autoResultMap = true)
public class Oss {
    private static final long serialVersionUID=1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String endpoint;
    private String workspace;
    private String accesskeyid;
    private String accesskeysecret;
    private String appid;
    private String region;
    private String type;
    private LocalDateTime createtime;
    private LocalDateTime updatetime;
}
