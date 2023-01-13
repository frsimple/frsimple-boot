package org.simple.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * User
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDto extends BaseEntity {

    private String username;

    private String nickName;

    private String phone;

    private String email;

    private String status;

    private String avatar;

    private Date loginDate;

    private String tenant;

    private String organ;
}