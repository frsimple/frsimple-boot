package org.simple.system.dto.code;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.PageModel;

/**
 * CodeQuery
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/12/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TableQuery extends PageModel {
    private String dataId;
    private String tableName;
}
