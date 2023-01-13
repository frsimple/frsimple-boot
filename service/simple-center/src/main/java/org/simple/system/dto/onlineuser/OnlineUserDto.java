package org.simple.system.dto.onlineuser;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * OnlineUserDto
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-11-27 22:06:40
 */
@Data
@Builder
public class OnlineUserDto {

    private String nickName;

    private String userName;

    private Date loginDate;

    private String timeout;

    private String userId;

    private String device;
}
