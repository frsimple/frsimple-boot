package org.simple.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * QueryOnlineUserDto
 *
 * @author xiaozhi
 * @version v1.0
 * @since 2022-11-27 22:06:40
 */
@Data
@Builder
public class QueryOnlineUserDto {

    private String nickName;

    private String userName;

    private Date loginDate;

    private String timeout;

    private String userId;
}
