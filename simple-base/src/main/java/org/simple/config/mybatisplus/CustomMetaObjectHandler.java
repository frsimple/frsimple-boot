//package org.simple.config.mybatisplus;
//
//import org.simple.constant.CurrentConst;
//import org.simple.enums.system.IsDeletedEnum;
//import org.simple.dto.UserInfo;
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import lombok.RequiredArgsConstructor;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
///**
// * MyBatisPlus字段自动填充
// *
// * @author yh_liu
// * @version v1.0
// * @since 2022/7/7
// */
//@Component
//@RequiredArgsConstructor
//public class CustomMetaObjectHandler implements MetaObjectHandler {
//
//    //private final CurrentUtil currentUtil;
//
//    /**
//     * 新增自动填充字段
//     *
//     * @param metaObject 元对象
//     */
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        // 起始版本 3.3.3(推荐)
//        UserInfo userInfo = currentUtil.getUserInfo();
//        this.strictInsertFill(metaObject, CurrentConst.CREATE_TIME, Date::new, Date.class);
//        this.strictInsertFill(metaObject, CurrentConst.CREATE_USER_ID, userInfo::getUserId, Long.class);
//        this.strictInsertFill(metaObject, CurrentConst.CREATE_USER_NAME, userInfo::getRealName, String.class);
//        this.strictInsertFill(metaObject, CurrentConst.CREATE_ORG_ID, userInfo::getOrgId, Long.class);
//        this.strictInsertFill(metaObject, CurrentConst.IS_DELETED, IsDeletedEnum.False::getKey, String.class);
//    }
//
//    /**
//     * 更新自动填充字段
//     *
//     * @param metaObject 元对象
//     */
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        // 起始版本 3.3.3(推荐)
//        UserInfo userInfo = currentUtil.getUserInfo();
//        this.strictUpdateFill(metaObject, CurrentConst.MODIFY_TIME, Date::new, Date.class);
//        this.strictUpdateFill(metaObject, CurrentConst.MODIFY_USER_ID, userInfo::getUserId, Long.class);
//        this.strictUpdateFill(metaObject, CurrentConst.MODIFY_USER_NAME, userInfo::getRealName, String.class);
//    }
//}
