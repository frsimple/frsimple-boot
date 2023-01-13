package org.simple.system.dto.email;

import lombok.Data;

/**
 * EmailParams
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
public class EmailQuery {

    private String title;
    private String content;
    private String tos;
    private boolean isHtml;
}
