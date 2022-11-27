package org.simple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * User
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@TableName("center_user")
public class User {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String username;

    private String password;

    private String nickname;

    private String phone;

    private String email;

    private String status;

    private String avatar;

    private LocalDateTime createdate;

    private LocalDateTime updatedate;

    private LocalDateTime logindate;

    /**
     * 主机构
     */
    private String tenant;

    private Integer errorcount;

    private LocalDateTime locktime;

    private String organ;
}