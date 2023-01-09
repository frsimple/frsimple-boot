package org.simple.constant;

import lombok.Data;

/**
 * 当前信息常量
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/23
 */
@Data
public class CurrentConst {

    /**
     * 主键id
     */
    public static final String ID = "id";

    /**
     * 创建时间
     */
    public static final String CREATE_TIME = "createTime";

    /**
     * 创建人id
     */
    public static final String CREATE_USER_ID = "createUserId";

    /**
     * 创建人
     */
    public static final String CREATE_USER_NAME = "createUserName";

    /**
     * 创建人组织id
     */
    public static final String CREATE_ORG_ID = "createOrgId";

    /**
     * 修改时间
     */
    public static final String MODIFY_TIME = "modifyTime";

    /**
     * 修改人id
     */
    public static final String MODIFY_USER_ID = "modifyUserId";

    /**
     * 修改人
     */
    public static final String MODIFY_USER_NAME = "modifyUserName";

    /**
     * 说明
     */
    public static final String DESCRIPTION = "description";

    /**
     * 排序号
     */
    public static final String SORT_INDEX = "sortIndex";

    /**
     * 是否删除
     */
    public static final String IS_DELETED = "isDeleted";
}
