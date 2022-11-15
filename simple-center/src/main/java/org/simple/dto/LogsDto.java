package org.simple.dto;

import lombok.Data;
import org.simple.entity.Logs;

/**
 * LogsDto
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Data
public class LogsDto extends Logs {

    private String nickName;

}
