package org.simple.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * SimpleUser
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
public class SimpleUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String nickname;

    private String username;

    private LocalDateTime loginDate;

    private String tenantId;

    private String clientId;
}
