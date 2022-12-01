package org.simple.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.entity.TableCfg;
import org.simple.utils.CommonResult;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TableCfgService extends IService<TableCfg> {

    /**
     * 动态连接数据库
     */
    public CommonResult queryTableList(String dataId, String tableName) throws SQLException;

    public byte[] codeCreate(String tableCfgId, String tableComment) throws SQLException;

    public List<Map<String, String>> queryTableColumns(String dataId, String tableName) throws SQLException;

    public boolean checkCon(String url, String user, String pwd);
}