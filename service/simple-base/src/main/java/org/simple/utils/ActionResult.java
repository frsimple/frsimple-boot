package org.simple.utils;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.simple.enums.system.ResultCodeEnum;

/**
 * 统一返回值对象
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/7
 */
@Data
public class ActionResult<T> {

    /**
     * 时间戳
     */
    @Schema(description = "时间戳")
    public long timestamp;

    /**
     * 状态码
     */
    @Schema(description = "状态码")
    private String statusCode;

    /**
     * 执行成功
     */
    @Schema(description = "执行成功")
    private Boolean succeeded;

    /**
     * 返回数据
     */
    @Schema(description = "返回数据")
    private Object data;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息")
    private String message;

    /**
     * 附加数据
     */
    @Schema(description = "附加数据")
    private Object extras;

    /**
     * @param <T> 实体类
     * @return 成功返回
     */
    public static <T> ActionResult<T> success() {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setStatusCode(ResultCodeEnum.SUCCESS.getCode());
        jsonData.setSucceeded(true);
        jsonData.setMessage(ResultCodeEnum.SUCCESS.getMsg());
        jsonData.setData("");
        jsonData.setExtras("");
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @return 成功返回
     */
    public static <T> ActionResult<T> success(String statusCode) {
        return getActionResult(statusCode);
    }

    /**
     * @param <T>  实体类
     * @param data 数据
     * @return 成功返回
     */
    public static <T> ActionResult<T> success(Object data) {
        return getActionResult(ResultCodeEnum.SUCCESS.getCode(), data);
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @return 成功返回
     */
    public static <T> ActionResult<T> success(String statusCode, Object data) {
        return getActionResult(statusCode, data);
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @param extras     扩展数据
     * @return 成功返回
     */
    public static <T> ActionResult<T> success(String statusCode, Object data, Object extras) {
        return getActionResult(statusCode, data, extras);
    }

    /**
     * @param <T> 实体类
     * @return 失败返回
     */
    public static <T> ActionResult<T> failed() {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setStatusCode(ResultCodeEnum.FAILED.getCode());
        jsonData.setSucceeded(true);
        jsonData.setMessage(ResultCodeEnum.FAILED.getMsg());
        jsonData.setData("");
        jsonData.setExtras("");
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @return 失败返回
     */
    public static <T> ActionResult<T> failed(String statusCode) {
        return getActionResult(statusCode);
    }

    /**
     * @param <T>  实体类
     * @param data 数据
     * @return 失败返回
     */
    public static <T> ActionResult<T> failed(Object data) {
        return getActionResult(ResultCodeEnum.FAILED.getCode(), data);
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @return 失败返回
     */
    public static <T> ActionResult<T> failed(String statusCode, Object data) {
        return getActionResult(statusCode, data);
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @param extras     扩展数据
     * @return 失败返回
     */
    public static <T> ActionResult<T> failed(String statusCode, Object data, Object extras) {
        return getActionResult(statusCode, data, extras);
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @return 获取返回信息
     */
    private static <T> ActionResult<T> getActionResult(String statusCode) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setStatusCode(statusCode);
        jsonData.setSucceeded(true);
        jsonData.setMessage(ResultCodeEnum.fromCode(statusCode).getMsg());
        jsonData.setData("");
        jsonData.setExtras("");
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @return 获取返回信息
     */
    private static <T> ActionResult<T> getActionResult(String statusCode, Object data) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setStatusCode(statusCode);
        jsonData.setSucceeded(true);
        jsonData.setMessage(ResultCodeEnum.fromCode(statusCode).getMsg());
        jsonData.setData(data);
        jsonData.setExtras("");
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @param extras     扩展数据
     * @return 失败返回
     */
    private static <T> ActionResult<T> getActionResult(String statusCode, Object data, Object extras) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setStatusCode(statusCode);
        jsonData.setSucceeded(true);
        jsonData.setMessage(ResultCodeEnum.fromCode(statusCode).getMsg());
        jsonData.setData(data);
        jsonData.setExtras(extras);
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }
}
