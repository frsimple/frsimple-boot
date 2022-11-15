package org.simple.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.simple.entity.User;

/**
 * UserDto
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
public class UserDto extends User {

    private String tenantName;
    private String roles;
    private String roleIds;

    @JsonProperty("nPassword")
    private String nPassword;
}
