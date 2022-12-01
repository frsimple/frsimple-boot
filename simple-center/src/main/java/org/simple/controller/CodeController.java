package org.simple.controller;


import cn.hutool.core.io.IoUtil;
import org.apache.commons.lang.StringUtils;
import org.simple.entity.DataSource;
import org.simple.entity.TableCfg;
import org.simple.service.DataSourceService;
import org.simple.service.TableCfgService;
import org.simple.utils.CommonResult;
import org.simple.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/center/code")
public class CodeController {


    @Autowired
    private DataSourceService dataSourceService;


    @Autowired
    private TableCfgService tableCfgService;


    /**
     * 查询数据源
     */
    @GetMapping("queryDs")
    public CommonResult queryDs() {
        return CommonResult.success(dataSourceService.list());
    }

    /**
     * 查询数据源表
     */
    @GetMapping("queryTableList")
    public CommonResult queryTableList(@RequestParam("dataId") String dataId,
                                       @RequestParam("tableName") String tableName) throws SQLException {
        return tableCfgService.queryTableList(dataId, tableName);
    }

    /**
     * 修改表配置信息
     */
    @PostMapping("updateTableCfg")
    public CommonResult updateTableCfg(@RequestBody TableCfg tableCfg) {
        if (StringUtils.isEmpty(tableCfg.getId())) {
            tableCfg.setId(RandomUtil.getTableCfg());
            tableCfg.setCreatetime(LocalDateTime.now());
            tableCfgService.save(tableCfg);
        } else {
            tableCfgService.updateById(tableCfg);
        }
        return CommonResult.successNodata("修改成功!");
    }


    @GetMapping("codeCreate")
    public void codeCreate(@RequestParam("id") String id,
                           @RequestParam("tableComment") String tableComment,
                           HttpServletResponse response) throws SQLException, IOException {
        byte[] data = tableCfgService.codeCreate(id, tableComment);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=1.zip");
        response.setContentType("application/zip; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
    }

    @PostMapping("addDataSource")
    public CommonResult addDataSource(@RequestBody DataSource dataSource) {
        if (!tableCfgService.checkCon(dataSource.getUrl(), dataSource.getUser(), dataSource.getPwd())) {
            return CommonResult.failed("数据源连接失败");
        }
        dataSource.setId(RandomUtil.getDataSource());
        dataSourceService.save(dataSource);
        return CommonResult.successNodata("保存成功");
    }


    @DeleteMapping("delDataSource")
    public CommonResult delDataSource(@RequestParam("id") String id) {
        dataSourceService.removeById(id);
        return CommonResult.successNodata("删除成功");
    }

    @PostMapping("checkDataSource")
    public CommonResult checkDataSource(@RequestBody DataSource dataSource) {
        if (!tableCfgService.checkCon(dataSource.getUrl(), dataSource.getUser(), dataSource.getPwd())) {
            return CommonResult.failed("数据源连接失败");
        } else {
            return CommonResult.successNodata("连接成功");
        }
    }

}
