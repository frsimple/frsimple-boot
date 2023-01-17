package org.simple.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022-7-24
 */

public class RandomUtil {

    public static String getTransId() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(5);
        return str;
    }

    /**
     * 获取应用id
     */
    public static String getAppid() {
        String str = "2019";
        str += String.valueOf(System.currentTimeMillis());
        str += String.valueOf(cn.hutool.core.util.RandomUtil.randomNumbers(5));
        return str;
    }

    /**
     * 获取商户编号/机构id
     */
    public static String getStoreId() {
        String str = "2022";
        str += String.valueOf(System.currentTimeMillis());
        str += String.valueOf(cn.hutool.core.util.RandomUtil.randomNumbers(5));
        return str;
    }

    /**
     * 获取支付宝授权码主键
     */
    public static String getAuthId() {
        String str = "3000";
        str += String.valueOf(System.currentTimeMillis());
        str += String.valueOf(cn.hutool.core.util.RandomUtil.randomNumbers(5));
        return str;
    }

    /**
     * 获取签约id
     */
    public static String getSignId() {
        String str = "3001";
        str += String.valueOf(System.currentTimeMillis());
        str += String.valueOf(cn.hutool.core.util.RandomUtil.randomNumbers(5));
        return str;
    }


    public static String getUuid() {
        return new Date().getTime() + "" + cn.hutool.core.util.RandomUtil.randomString(5).toUpperCase();
    }



    /**
     * 获取商户编号/机构id
     */
    public static String getStoreSubId() {
        String str = "3022";
        str += String.valueOf(System.currentTimeMillis());
        str += String.valueOf(cn.hutool.core.util.RandomUtil.randomNumbers(5));
        return str;
    }

    public static String getFlUuid() {
        String str = "FL";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(8).toUpperCase();
        return str;
    }

    /**
     * 获取订单流水号
     */
    public static String getOrderId() {
        String str = "5200";
        str += String.valueOf(System.currentTimeMillis());
        str += String.valueOf(cn.hutool.core.util.RandomUtil.randomNumbers(5));
        return str;
    }

    /**
     * 获取第三方应用用户信息主键
     */
    public static String getThirUserid() {
        String str = "TU22";
        str += String.valueOf(System.currentTimeMillis());
        str += String.valueOf(cn.hutool.core.util.RandomUtil.randomNumbers(5));
        return str;
    }

    /**
     * 生成支付宝订单号
     */
    public static String getAliTransNo() {
        String str = "A";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(10).toUpperCase();
        return str;
    }

    /**
     * 生成微信订单号
     */
    public static String getWxTransNo() {
        String str = "W";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(10).toUpperCase();
        return str;
    }

    /**
     * 日志id
     */
    public static String getLogsId() {
        String str = "LOG";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(5).toUpperCase();
        return str;
    }

    /**
     * 菜单id
     */
    public static String getMenuId() {
        String str = "MENU";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(5).toUpperCase();
        return str;
    }

    /**
     * 按钮菜单id
     */
    public static String getMenuBtnId() {
        String str = "BTN";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(5).toUpperCase();
        return str;
    }

    /**
     * 权限id
     */
    public static String getRoleId() {
        String str = "ROLE";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(5).toUpperCase();
        return str;
    }

    /**
     * 权限菜单id
     */
    public static String getRoleMenuId() {
        String str = "RM";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(5).toUpperCase();
        return str;
    }

    /**
     * 权限用户id
     */
    public static String getRoleUserId() {
        String str = "RU";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(5).toUpperCase();
        return str;
    }

    /**
     * 字典ID
     */
    public static String getDictId() {
        String str = "D";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(5).toUpperCase();
        return str;
    }

    /**
     * 机构ID
     */
    public static String getTenantId() {
        String str = "T";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(8).toUpperCase();
        return str;
    }

    /**
     * 用户ID
     */
    public static String getUserId() {
        String str = "U";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(8).toUpperCase();
        return str;
    }

    /**
     * 用户机构关系表id
     */
    public static String getUserTenantId() {
        String str = "UT";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(8).toUpperCase();
        return str;
    }

    /**
     * 获取oss主键id
     */
    public static String getOssId() {
        String str = "OSS";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(5).toUpperCase();
        return str;
    }

    /**
     * 获取邮件配置表id
     */
    public static String getEmailCfgId() {
        String str = "EM";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(5).toUpperCase();
        return str;
    }

    /**
     * 获取短信配置id
     */
    public static String getSmsCfgId() {
        String str = "SMS";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(5).toUpperCase();
        return str;
    }

    /**
     * 生成小程序授权码
     */
    public static String getQrcode() {
        return cn.hutool.core.util.RandomUtil.randomString(12);
    }

    /**
     * 代码生成表配置主键
     */
    public static String getTableCfg() {
        String str = "TC";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        str += sdf.format(new Date());
        return str;
    }

    /**
     * 代码生成表配置主键
     */
    public static String getDataSource() {
        String str = "DS";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        str += sdf.format(new Date());
        return str;
    }


    /**
     * 组织机构ID
     */
    public static String getOrganId() {
        String str = "OR";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(8).toUpperCase();
        return str;
    }


    /**
     * 设计师名片- 分类id
     */
    public static String getWxCataid() {
        String str = "CA";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(8).toUpperCase();
        return str;
    }

    public static String getWxStarid() {
        String str = "ST";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(8).toUpperCase();
        return str;
    }

    public static String getWxUserid() {
        String str = "WX";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(8).toUpperCase();
        return str;
    }

    public static String getProductId() {
        String str = "PROD";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDD");
        str += sdf.format(new Date());
        str += cn.hutool.core.util.RandomUtil.randomString(6).toUpperCase();
        return str;
    }
}
