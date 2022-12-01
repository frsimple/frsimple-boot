package org.simple.service.impl;


import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.simple.dto.TableInfo;
import org.simple.entity.DataSource;
import org.simple.entity.TableCfg;
import org.simple.mapper.TableCfgMapper;
import org.simple.service.DataSourceService;
import org.simple.service.TableCfgService;
import org.simple.util.GeneratorUtil;
import org.simple.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Service
public class TableCfgServiceImpl
        extends ServiceImpl<TableCfgMapper, TableCfg>
        implements TableCfgService {

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public CommonResult queryTableList(String dataId, String tableName) throws SQLException {
        //查询动态数据库配置数据
        DataSource dataSource = dataSourceService.getById(dataId);
        //开始连接数据库
        Connection conn = null;
        PreparedStatement pstmt = null;
        List<TableInfo> tableList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2.获取连接
            conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUser(), dataSource.getPwd());
            //4.定义sql
            String sql = " select table_comment,table_collation,create_time,table_name from" +
                    " information_schema.tables  where table_schema = (select database())";
            if (StringUtils.isNotEmpty(tableName)) {
                sql += " and table_name like ? ";
            }
            //5.获取pstmt对象
            pstmt = conn.prepareStatement(sql);
            if (StringUtils.isNotEmpty(tableName)) {
                pstmt.setString(1, "%" + tableName + "%");
            }
            //7.执行sql
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(rs.getString("table_name"));
                tableInfo.setTableCollation(rs.getString("table_collation"));
                Date date = rs.getDate("create_time");
                tableInfo.setCreateTime(date);
                tableInfo.setTableComment(rs.getString("table_comment"));

                TableCfg cfg = new TableCfg();
                cfg.setDatasource(dataId);
                cfg.setTablename(rs.getString("table_name"));
                List<TableCfg> cfgList = baseMapper.selectList(Wrappers.query(cfg));
                if (cfgList.size() != 0) {
                    cfg = cfgList.get(0);
                    tableInfo.setId(cfg.getId());
                    tableInfo.setAuth(cfg.getAuth());
                    tableInfo.setPkg(cfg.getPkg());
                    tableInfo.setFix(cfg.getFix());
                }
                tableList.add(tableInfo);
            }
            //return CommonResult.success(tableList);
        } catch (Exception ex) {
            return CommonResult.failed(ex.getMessage());
        } finally {
            pstmt.close();
            conn.close();
        }
        return CommonResult.success(tableList);
    }

    @Override
    public byte[] codeCreate(String tableCfgId, String tableComment) throws SQLException {
        TableCfg tableCfg = baseMapper.selectById(tableCfgId);
        Map<String, String> result = new HashMap<>();
        result.put("tableName", tableCfg.getTablename());
        result.put("tableComment", tableComment);
        result.put("tablePrefix", tableCfg.getFix());
        result.put("package", tableCfg.getPkg());
        result.put("author", tableCfg.getAuth());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        List<Map<String, String>> columns = queryTableColumns(tableCfg.getDatasource(), tableCfg.getTablename());
        //生成代码
        GeneratorUtil.generatorCode(result, columns, zipOutputStream);
        IoUtil.close(zipOutputStream);
        byte[] rs = byteArrayOutputStream.toByteArray();
        return rs;
    }


    @Override
    public List<Map<String, String>> queryTableColumns(String dataId, String tableName) throws SQLException {
        //查询动态数据库配置数据
        DataSource dataSource = dataSourceService.getById(dataId);
        //开始连接数据库
        Connection conn = null;
        PreparedStatement pstmt = null;
        List<Map<String, String>> result = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2.获取连接
            conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUser(), dataSource.getPwd());
            //4.定义sql
            String sql = "select column_name, data_type, column_comment, column_key, extra from information_schema.columns" +
                    " where table_name = ? and table_schema = (select database()) order by ordinal_position";
            //5.获取pstmt对象
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tableName);
            //7.执行sql
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("columnName", rs.getString("column_name"));
                map.put("dataType", rs.getString("data_type"));
                map.put("columnComment", rs.getString("column_comment"));
                map.put("extra", rs.getString("extra"));
                map.put("columnKey", rs.getString("column_key"));
                result.add(map);
            }
        } catch (Exception ex) {
            //return CommonResult.failed(ex.getMessage());
        } finally {
            pstmt.close();
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
