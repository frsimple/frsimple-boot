package org.simple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Tenant
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@Accessors(chain = true)
@TableName(value = "center_tenant", autoResultMap = true)
public class Tenant {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String name;
    private String phone;
    private String email;
    private String address;
    private String status;
    private LocalDateTime updatedate;
    private LocalDateTime createdate;

}
