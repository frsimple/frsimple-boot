package org.simple.system.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simple.dto.PageModel;

/**
 * UserQuery
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends PageModel {

    private String tenant;
    private String nickName;
    private String status;
}
