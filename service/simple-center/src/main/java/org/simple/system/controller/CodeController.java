package org.simple.system.controller;

import cn.hutool.core.io.IoUtil;
import com.github.yitter.idgen.YitIdHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.simple.dto.PageResult;
import org.simple.system.dto.code.DownloadDto;
import org.simple.system.dto.code.TableQuery;
import org.simple.system.dto.table.TableInfo;
import org.simple.system.entity.DataSourceEntity;
import org.simple.system.entity.TableCfgEntity;
import org.simple.enums.system.ResultCodeEnum;
import org.simple.system.service.IDataSourceService;
import org.simple.system.service.TableCfgService;
import org.simple.utils.CommonResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 代码生成
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/12/2
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/system/code")
@Tag(name = "code", description = "代码生成")
public class CodeController {
    private final IDataSourceService IDataSourceService;
    private final TableCfgService tableCfgService;

    /**
     * 查询数据源
     */
    @GetMapping("queryDs")
    @Operation(summary = "查询数据源")
    public List<DataSourceEntity> queryDs() {
        return IDataSourceService.list();
    }

    /**
     * 查询数据源表
     */
    @GetMapping("queryTableList")
    @Operation(summary = "查询数据源表")
    public PageResult<TableInfo> queryTableList(TableQuery query) throws SQLException {
        List<TableInfo> list = tableCfgService.queryTableList(query);
        PageResult<TableInfo> pageResult = new PageResult<>(list);
        pageResult.setTotal(query.getTotal());
        return pageResult;
    }

    /**
     * 修改表配置信息
     */
    @PostMapping("updateTableCfg")
    @Operation(summary = "修改表配置信息")
    public CommonResult updateTableCfg(@RequestBody TableCfgEntity tableCfgEntity) {
        if (StringUtils.isEmpty(tableCfgEntity.getId())) {
            tableCfgEntity.setId(String.valueOf(YitIdHelper.nextId()));
            tableCfgService.save(tableCfgEntity);
        } else {
            tableCfgService.updateById(tableCfgEntity);
        }
        return CommonResult.success();
    }


    @GetMapping("codeCreate")
    @Operation(summary = "生成代码")
    public void codeCreate(DownloadDto downloadDto, HttpServletResponse response) throws SQLException, IOException {
        byte[] data = tableCfgService.codeCreate(downloadDto.getId(), downloadDto.getTableComment());
        response.setHeader("Content-Disposition", "attachment; filename=1.zip");
        response.setContentType("application/zip; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
    }

    @PostMapping("addDataSource")
    @Operation(summary = "新建数据源")
    public CommonResult addDataSource(@RequestBody DataSourceEntity dataSourceEntity) {
        if (!tableCfgService.checkCon(dataSourceEntity.getUrl(), dataSourceEntity.getUser(), dataSourceEntity.getPwd())) {
            return CommonResult.failed(ResultCodeEnum.DB5002.getMsg());
        }
        dataSourceEntity.setId(String.valueOf(YitIdHelper.nextId()));
        IDataSourceService.save(dataSourceEntity);
         return CommonResult.success();
    }



    @PostMapping("delDataSource")
    @Operation(summary = "删除数据源")
    public CommonResult delDataSource(@RequestParam("id") String id) {
        IDataSourceService.removeById(id);
        return  CommonResult.success();
    }

    @PostMapping("checkDataSource")
    @Operation(summary = "检查数据源")
    public CommonResult checkDataSource(@RequestBody DataSourceEntity dataSourceEntity) {
        if (!tableCfgService.checkCon(dataSourceEntity.getUrl(), dataSourceEntity.getUser(), dataSourceEntity.getPwd())) {
            return CommonResult.failed(ResultCodeEnum.DB5002.getMsg());
        } else {
            return CommonResult.success();
        }
    }
}
