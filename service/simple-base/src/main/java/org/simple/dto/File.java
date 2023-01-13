package org.simple.dto;


import lombok.Data;

import java.util.Date;

/**
 * File
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
public class File {

    private Long size;
    private Date updateDate;
    private String key;
}
