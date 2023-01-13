package org.simple.system.dto.code;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CodeQuery
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/12/7
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DownloadDto {
    private String id;
    private String tableComment;
}
