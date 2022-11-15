package org.simple.dto;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 22699
 */
@Data
public class OrganTreeDto {

    private String id;
    private String name;
    private String parentid;
    private String parentname;
    private String tenantname;
    private String tenantid;
    private String sort;
    private LocalDateTime createtime;

}
