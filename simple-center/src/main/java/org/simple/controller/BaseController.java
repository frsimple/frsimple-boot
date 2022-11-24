package org.simple.controller;

import cn.dev33.satoken.stp.StpUtil;

public class BaseController {


    /**
     * 获取当前登录的用户ID
     * @return
     */
    protected Object getCurrentUserId (){
        return StpUtil.getLoginId();
    }


    /**
     * 获取当前用户的token
     * @return
     */
    protected String getCurrentToken(){
        return  StpUtil.getTokenValue();
    }
}
