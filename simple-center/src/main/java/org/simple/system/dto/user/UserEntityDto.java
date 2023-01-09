package org.simple.system.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.system.entity.UserEntity;

/**
 * UserDto
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserEntityDto extends UserEntity {

    private String tenantName;
    private String roles;
    private String roleIds;

    @JsonProperty("nPassword")
    private String nPassword;
}
