//package org.simple.utils;
//
//import cn.hutool.core.date.DateUtil;
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Data;
//import org.simple.enums.system.ResultCodeEnum;
//
///**
// * 统一返回值对象
// *
// * @author yh_liu
// * @version v1.0
// * @since 2022/7/7
// */
//@Data
//public class CommonResult<T> {
//
//    /**
//     * 时间戳
//     */
//    @Schema(description = "时间戳")
//    public long timestamp;
//
//    /**
//     * 状态码
//     */
//    @Schema(description = "状态码")
//    private String statusCode;
//
//    /**
//     * 执行成功
//     */
//    @Schema(description = "执行成功")
//    private Boolean succeeded;
//
//    /**
//     * 返回数据
//     */
//    @Schema(description = "返回数据")
//    private Object data;
//
//    /**
//     * 错误信息
//     */
//    @Schema(description = "错误信息")
//    private String message;
//
//    /**
//     * 附加数据
//     */
//    @Schema(description = "附加数据")
//    private Object extras;
//
//    /**
//     * @param <T> 实体类
//     * @return 成功返回
//     */
//    public static <T> CommonResult<T> success() {
//        CommonResult<T> jsonData = new CommonResult<>();
//        jsonData.setStatusCode(ResultCodeEnum.SUCCESS.getCode());
//        jsonData.setSucceeded(true);
//        jsonData.setMessage(ResultCodeEnum.SUCCESS.getMsg());
//        jsonData.setData("");
//        jsonData.setExtras("");
//        jsonData.setTimestamp(DateUtil.current());
//        return jsonData;
//    }
//
//    /**
//     * @param <T>        实体类
//     * @param statusCode 状态码
//     * @return 成功返回
//     */
//    public static <T> CommonResult<T> success(String statusCode) {
//        return getCommonResult(statusCode);
//    }
//
//    /**
//     * @param <T>  实体类
//     * @param data 数据
//     * @return 成功返回
//     */
//    public static <T> CommonResult<T> success(Object data) {
//        return getCommonResult(ResultCodeEnum.SUCCESS.getCode(), data);
//    }
//
//    /**
//     * @param <T>        实体类
//     * @param statusCode 状态码
//     * @param data       返回数据
//     * @return 成功返回
//     */
//    public static <T> CommonResult<T> success(String statusCode, Object data) {
//        return getCommonResult(statusCode, data);
//    }
//
//    /**
//     * @param <T>        实体类
//     * @param statusCode 状态码
//     * @param data       返回数据
//     * @param extras     扩展数据
//     * @return 成功返回
//     */
//    public static <T> CommonResult<T> success(String statusCode, Object data, Object extras) {
//        return getCommonResult(statusCode, data, extras);
//    }
//
//    /**
//     * @param <T> 实体类
//     * @return 失败返回
//     */
//    public static <T> CommonResult<T> failed() {
//        CommonResult<T> jsonData = new CommonResult<>();
//        jsonData.setStatusCode(ResultCodeEnum.FAILED.getCode());
//        jsonData.setSucceeded(true);
//        jsonData.setMessage(ResultCodeEnum.FAILED.getMsg());
//        jsonData.setData("");
//        jsonData.setExtras("");
//        jsonData.setTimestamp(DateUtil.current());
//        return jsonData;
//    }
//
//    /**
//     * @param <T>        实体类
//     * @param statusCode 状态码
//     * @return 失败返回
//     */
//    public static <T> CommonResult<T> failed(String statusCode) {
//        return getCommonResult(statusCode);
//    }
//
//    /**
//     * @param <T>  实体类
//     * @param data 数据
//     * @return 失败返回
//     */
//    public static <T> CommonResult<T> failed(Object data) {
//        return getCommonResult(ResultCodeEnum.FAILED.getCode(), data);
//    }
//
//    /**
//     * @param <T>        实体类
//     * @param statusCode 状态码
//     * @param data       返回数据
//     * @return 失败返回
//     */
//    public static <T> CommonResult<T> failed(String statusCode, Object data) {
//        return getCommonResult(statusCode, data);
//    }
//
//    /**
//     * @param <T>        实体类
//     * @param statusCode 状态码
//     * @param data       返回数据
//     * @param extras     扩展数据
//     * @return 失败返回
//     */
//    public static <T> CommonResult<T> failed(String statusCode, Object data, Object extras) {
//        return getCommonResult(statusCode, data, extras);
//    }
//
//    /**
//     * @param <T>        实体类
//     * @param statusCode 状态码
//     * @return 获取返回信息
//     */
//    private static <T> CommonResult<T> getCommonResult(String statusCode) {
//        CommonResult<T> jsonData = new CommonResult<>();
//        jsonData.setStatusCode(statusCode);
//        jsonData.setSucceeded(true);
//        jsonData.setMessage(ResultCodeEnum.fromCode(statusCode).getMsg());
//        jsonData.setData("");
//        jsonData.setExtras("");
//        jsonData.setTimestamp(DateUtil.current());
//        return jsonData;
//    }
//
//    /**
//     * @param <T>        实体类
//     * @param statusCode 状态码
//     * @param data       返回数据
//     * @return 获取返回信息
//     */
//    private static <T> CommonResult<T> getCommonResult(String statusCode, Object data) {
//        CommonResult<T> jsonData = new CommonResult<>();
//        jsonData.setStatusCode(statusCode);
//        jsonData.setSucceeded(true);
//        jsonData.setMessage(ResultCodeEnum.fromCode(statusCode).getMsg());
//        jsonData.setData(data);
//        jsonData.setExtras("");
//        jsonData.setTimestamp(DateUtil.current());
//        return jsonData;
//    }
//
//    /**
//     * @param <T>        实体类
//     * @param statusCode 状态码
//     * @param data       返回数据
//     * @param extras     扩展数据
//     * @return 失败返回
//     */
//    private static <T> CommonResult<T> getCommonResult(String statusCode, Object data, Object extras) {
//        CommonResult<T> jsonData = new CommonResult<>();
//        jsonData.setStatusCode(statusCode);
//        jsonData.setSucceeded(true);
//        jsonData.setMessage(ResultCodeEnum.fromCode(statusCode).getMsg());
//        jsonData.setData(data);
//        jsonData.setExtras(extras);
//        jsonData.setTimestamp(DateUtil.current());
//        return jsonData;
//    }
//}
