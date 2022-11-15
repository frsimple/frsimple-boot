package org.simple.dto;

import lombok.Data;

/**
 * EmailParams
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
public class EmailParams {

    private String title;
    private String content;
    private String tos;
    private boolean isHtml;
}
