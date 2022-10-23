package org.simple.model;

import cn.hutool.core.date.DateUtil;
import org.simple.enums.system.ErrorCodesEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
        jsonData.setStatusCode(ErrorCodesEnum.A200.getKey());
        jsonData.setSucceeded(true);
        jsonData.setMessage(ErrorCodesEnum.A200.getValue());
        jsonData.setData("");
        jsonData.setExtras("");
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }

    /**
     * @param <T>     实体类
     * @param message 错误信息
     * @return 成功返回
     */
    public static <T> ActionResult<T> success(String message) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setStatusCode(ErrorCodesEnum.A200.getKey());
        jsonData.setSucceeded(true);
        jsonData.setMessage(message);
        jsonData.setData("");
        jsonData.setExtras("");
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param message    错误信息
     * @return 成功返回
     */
    public static <T> ActionResult<T> success(String statusCode, String message) {
        return getActionResult(statusCode, message);
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @param message    错误信息
     * @return 成功返回
     */
    public static <T> ActionResult<T> success(String statusCode, Object data, String message) {
        return getActionResult(statusCode, data, message);
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @param message    错误信息
     * @param extras     扩展数据
     * @return 成功返回
     */
    public static <T> ActionResult<T> success(String statusCode, Object data, String message, Object extras) {
        return getActionResult(statusCode, data, message, extras);
    }

    /**
     * @param <T> 实体类
     * @return 失败返回
     */
    public static <T> ActionResult<T> fail() {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setStatusCode(ErrorCodesEnum.A400.getKey());
        jsonData.setSucceeded(true);
        jsonData.setMessage(ErrorCodesEnum.A400.getValue());
        jsonData.setData("");
        jsonData.setExtras("");
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }

    /**
     * @param <T>     实体类
     * @param message 错误信息
     * @return 失败返回
     */
    public static <T> ActionResult<T> fail(String message) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setStatusCode(ErrorCodesEnum.A400.getKey());
        jsonData.setSucceeded(true);
        jsonData.setMessage(message);
        jsonData.setData("");
        jsonData.setExtras("");
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param message    错误信息
     * @return 失败返回
     */
    public static <T> ActionResult<T> fail(String statusCode, String message) {
        return getActionResult(statusCode, message);
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @param message    错误信息
     * @return 失败返回
     */
    public static <T> ActionResult<T> fail(String statusCode, Object data, String message) {
        return getActionResult(statusCode, data, message);
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @param message    错误信息
     * @param extras     扩展数据
     * @return 失败返回
     */
    public static <T> ActionResult<T> fail(String statusCode, Object data, String message, Object extras) {
        return getActionResult(statusCode, data, message, extras);
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param message    错误信息
     * @return 获取返回信息
     */
    private static <T> ActionResult<T> getActionResult(String statusCode, String message) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setStatusCode(statusCode);
        jsonData.setSucceeded(true);
        jsonData.setMessage(message);
        jsonData.setData("");
        jsonData.setExtras("");
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @param message    错误信息
     * @return 获取返回信息
     */
    private static <T> ActionResult<T> getActionResult(String statusCode, Object data, String message) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setStatusCode(statusCode);
        jsonData.setSucceeded(true);
        jsonData.setMessage(message);
        jsonData.setData(data);
        jsonData.setExtras("");
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }

    /**
     * @param <T>        实体类
     * @param statusCode 状态码
     * @param data       返回数据
     * @param message    错误信息
     * @param extras     扩展数据
     * @return 失败返回
     */
    private static <T> ActionResult<T> getActionResult(String statusCode, Object data, String message, Object extras) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setStatusCode(statusCode);
        jsonData.setSucceeded(true);
        jsonData.setMessage(message);
        jsonData.setData(data);
        jsonData.setExtras(extras);
        jsonData.setTimestamp(DateUtil.current());
        return jsonData;
    }
}
