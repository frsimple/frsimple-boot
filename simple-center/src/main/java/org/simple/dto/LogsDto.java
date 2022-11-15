package org.simple.dto;

import lombok.Data;
import org.simple.entity.Logs;

@Data
public class LogsDto extends Logs {

    private String nickName;

}
