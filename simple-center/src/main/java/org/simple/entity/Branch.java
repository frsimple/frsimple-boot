package org.simple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 组织实体
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/23
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "center_branch")
public class Branch implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String name;

    private String parentid;

    private LocalDateTime createtime;

    private Integer sort;

    private String tenantid;


}
