package org.simple.system.dto.dict;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.PageModel;

/**
 * DictQuery
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/12/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictQuery extends PageModel {
    private String code;

    private String value;
    private String label;
}
