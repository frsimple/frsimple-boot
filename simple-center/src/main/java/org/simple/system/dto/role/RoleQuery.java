package org.simple.system.dto.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.PageModel;

/**
 * RoleQuery
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-12-6 18:50:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleQuery extends PageModel {
    private String name;
}
