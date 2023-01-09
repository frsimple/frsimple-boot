package org.simple.system.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.simple.system.dto.code.TableQuery;
import org.simple.system.dto.table.TableInfo;
import org.simple.system.entity.DataSourceEntity;
import org.simple.system.entity.TableCfgEntity;
import org.simple.system.mapper.TableCfgMapper;
import org.simple.system.service.IDataSourceService;
import org.simple.system.service.TableCfgService;
import org.simple.system.util.GeneratorUtil;
import org.simple.utils.PageUtil;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * Sa-Token的自定义权限验证扩展
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Service
@AllArgsConstructor
public class TableCfgServiceImpl extends ServiceImpl<TableCfgMapper, TableCfgEntity> implements TableCfgService {
    private final IDataSourceService dataSourceService;

    @Override
    public List<TableInfo> queryTableList(TableQuery query) throws SQLException {
        //查询动态数据库配置数据
        DataSourceEntity dataSourceEntity = dataSourceService.getById(query.getDataId());
        //开始连接数据库
        Connection conn = null;
        PreparedStatement statement = null;
        List<TableInfo> tableList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2.获取连接
            conn = DriverManager.getConnection(dataSourceEntity.getUrl(), dataSourceEntity.getUser(), dataSourceEntity.getPwd());
            //4.定义sql
            String sql = " select table_comment,table_collation,create_time,table_name from" +
                    " information_schema.tables  where table_schema = (select database())";
            if (StringUtils.isNotEmpty(query.getTableName())) {
                sql += " and table_name like ? ";
            }
            //5.获取对象
            statement = conn.prepareStatement(sql);
            if (StringUtils.isNotEmpty(query.getTableName())) {
                statement.setString(1, "%" + query.getTableName() + "%");
            }
            //7.执行sql
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(rs.getString("table_name"));
                tableInfo.setTableCollation(rs.getString("table_collation"));
                Date date = rs.getDate("create_time");
                tableInfo.setCreateTime(date);
                tableInfo.setTableComment(rs.getString("table_comment"));

                TableCfgEntity cfg = new TableCfgEntity();
                cfg.setDatasource(query.getDataId());
                cfg.setTableName(rs.getString("table_name"));
                List<TableCfgEntity> cfgList = baseMapper.selectList(Wrappers.query(cfg));
                if (cfgList.size() != 0) {
                    cfg = cfgList.get(0);
                    tableInfo.setId(cfg.getId());
                    tableInfo.setAuth(cfg.getAuth());
                    tableInfo.setPkg(cfg.getPkg());
                    tableInfo.setFix(cfg.getFix());
                }
                tableList.add(tableInfo);
            }
        } catch (Exception ex) {
            return null;
        } finally {
            statement.close();
            conn.close();
        }
        if (StrUtil.isNotEmpty(query.getTableName())) {
            tableList = tableList.stream().filter(x -> x.getTableName().contains(query.getTableName())).collect(Collectors.toList());
        }
        return query.setList(PageUtil.getListPage(query.getCurrent(), query.getPageSize(), tableList), tableList.size());
    }

    @Override
    public byte[] codeCreate(String tableCfgId, String tableComment) throws SQLException {
        TableCfgEntity tableCfgEntity = baseMapper.selectById(tableCfgId);
        Map<String, String> result = new HashMap<>(10);
        result.put("tableName", tableCfgEntity.getTableName());
        result.put("tableComment", tableComment);
        result.put("tablePrefix", tableCfgEntity.getFix());
        result.put("package", tableCfgEntity.getPkg());
        result.put("author", tableCfgEntity.getAuth());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        List<Map<String, String>> columns = queryTableColumns(tableCfgEntity.getDatasource(), tableCfgEntity.getTableName());
        //生成代码
        GeneratorUtil.generatorCode(result, columns, zipOutputStream);
        IoUtil.close(zipOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    @Override
    public List<Map<String, String>> queryTableColumns(String dataId, String tableName) throws SQLException {
        //查询动态数据库配置数据
        DataSourceEntity dataSourceEntity = dataSourceService.getById(dataId);
        //开始连接数据库
        Connection conn = null;
        PreparedStatement statement = null;
        List<Map<String, String>> result = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2.获取连接
            conn = DriverManager.getConnection(dataSourceEntity.getUrl(), dataSourceEntity.getUser(), dataSourceEntity.getPwd());
            //4.定义sql
            String sql = "select column_name, data_type, column_comment, column_key, extra from information_schema.columns" +
                    " where table_name = ? and table_schema = (select database()) order by ordinal_position";
            //5.获取对象
            statement = conn.prepareStatement(sql);
            statement.setString(1, tableName);
            //7.执行sql
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Map<String, String> map = new HashMap<>(10);
                map.put("columnName", rs.getString("column_name"));
                map.put("dataType", rs.getString("data_type"));
                map.put("columnComment", rs.getString("column_comment"));
                map.put("extra", rs.getString("extra"));
                map.put("columnKey", rs.getString("column_key"));
                result.add(map);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            statement.close();
            conn.close();
        }
        return result;
    }

    @Override
    public boolean checkCon(String url, String user, String pwd) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.getConnection(url, user, pwd);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
