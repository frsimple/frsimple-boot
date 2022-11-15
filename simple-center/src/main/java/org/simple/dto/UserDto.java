package org.simple.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.simple.entity.User;

@Data
public class UserDto extends User {

    private String tenantName;
    private String roles;
    private String roleIds;

    @JsonProperty("nPassword")
    private String nPassword;
}
