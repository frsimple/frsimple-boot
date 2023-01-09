package org.simple.system.dto.onlineuser;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.PageModel;

/**
 * OnlineUserQuery
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-11-27 19:53:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OnlineUserQuery extends PageModel {
    private String name;
}
