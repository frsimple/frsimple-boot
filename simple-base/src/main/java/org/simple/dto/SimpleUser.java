package org.simple.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SimpleUser implements Serializable {
    private static final long serialVersionUID=1L;


    private String  id;

    private String  nickname;

    private String  username;

    private LocalDateTime loginDate;

    private String tenantId;

    private String clientId;
}
