package org.simple.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.simple.constant.CurrentConst;
import org.simple.dto.UserDto;
import org.simple.enums.system.IsDeletedEnum;
import org.simple.utils.AuthUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MyBatisPlus字段自动填充
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/7
 */
@Component
@RequiredArgsConstructor
public class CustomMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增自动填充字段
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        UserDto userDto = AuthUtils.getUser();
        this.strictInsertFill(metaObject, CurrentConst.CREATE_TIME, Date::new, Date.class);
        this.strictInsertFill(metaObject, CurrentConst.CREATE_USER_ID, userDto::getId, String.class);
        this.strictInsertFill(metaObject, CurrentConst.CREATE_USER_NAME, userDto::getNickName, String.class);
        this.strictInsertFill(metaObject, CurrentConst.CREATE_ORG_ID, userDto::getOrgan, String.class);
        this.strictInsertFill(metaObject, CurrentConst.IS_DELETED, IsDeletedEnum.False::getKey, String.class);
    }

    /**
     * 更新自动填充字段
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        UserDto userDto = AuthUtils.getUser();
        this.strictUpdateFill(metaObject, CurrentConst.MODIFY_TIME, Date::new, Date.class);
        this.strictUpdateFill(metaObject, CurrentConst.MODIFY_USER_ID, userDto::getId, String.class);
        this.strictUpdateFill(metaObject, CurrentConst.MODIFY_USER_NAME, userDto::getNickName, String.class);
    }
}
