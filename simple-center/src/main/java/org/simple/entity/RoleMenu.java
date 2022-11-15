package org.simple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * RoleMenu
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@Accessors(chain = true)
@TableName(value = "center_rolemenu", autoResultMap = true)
public class RoleMenu {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String role;
    private String menu;
}
