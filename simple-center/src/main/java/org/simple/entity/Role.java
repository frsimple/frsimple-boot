package org.simple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Role
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@Accessors(chain = true)
@TableName(value = "center_role", autoResultMap = true)
public class Role {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String name;
    private String remark;
    private String status;
    private String code;
    private String type;
    private String tenants;

    private LocalDateTime createtime;


}
