package org.simple.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.system.dto.code.TableQuery;
import org.simple.system.dto.table.TableInfo;
import org.simple.system.entity.TableCfgEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * TableCfgService
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
public interface TableCfgService extends IService<TableCfgEntity> {

    /**
     * 动态连接数据库
     */
    List<TableInfo> queryTableList(TableQuery query) throws SQLException;

    byte[] codeCreate(String tableCfgId, String tableComment) throws SQLException;

    List<Map<String, String>> queryTableColumns(String dataId, String tableName) throws SQLException;

    boolean checkCon(String url, String user, String pwd);
}