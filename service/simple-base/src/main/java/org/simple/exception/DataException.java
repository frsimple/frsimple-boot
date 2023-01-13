package org.simple.exception;

import org.simple.enums.system.ResultCodeEnum;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库异常类
 *
 * @author yh_liu
 * @since 2022/9/13 10:10
 */
public class DataException extends Exception {


    /**
     * 自定义数据异常
     */
    public DataException() {
        super();
    }

    /**
     * 自定义数据异常
     *
     * @param message 异常信息
     */
    public DataException(String message) {
        super(message);
    }

    /**
     * 自定义数据链接异常
     *
     * @param warning 异常信息
     * @return 数据异常
     */
    public static DataException errorLink(String warning) {
        return new DataException(ResultCodeEnum.DB5002.getMsg() + warning);
    }

    /**
     * 自定义数据回滚异常
     *
     * @param e            异常信息
     * @param rollbackConn 数据链接
     * @return sql异常
     */
    public static SQLException rollbackDataException(SQLException e, Connection rollbackConn) {
        executeRollback(rollbackConn);
        return e;
    }

    /**
     * 执行回滚
     *
     * @param conn 数据链接
     */
    private static void executeRollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
