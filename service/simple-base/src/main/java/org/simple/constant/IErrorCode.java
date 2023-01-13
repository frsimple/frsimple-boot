package org.simple.constant;

/**
 * 异常code枚举
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */

public interface IErrorCode {

    /**
     * 获取异常编号
     *
     * @return 异常编码
     */
    long getCode();

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    String getMsg();

}
