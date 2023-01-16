package org.simple.online.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.simple.dto.IdsModel;
import org.simple.dto.PageModel;
import org.simple.dto.PageResult;
import org.simple.online.dto.fieldarea.FieldAreaFormVO;
import org.simple.online.entity.FieldAreaEntity;
import org.simple.online.service.IFieldAreaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据域定义 控制器
 *
 * @author ThePai
 * @since 2022-8-7
 */
@Tag(name = "fieldArea", description = "数据域定义")
@RestController
@RequestMapping("/center/online/fieldArea")
@RequiredArgsConstructor
public class FieldAreaController {
    private final IFieldAreaService fieldAreaService;

    /**
     * 查询列表分页
     *
     * @param pageModel 分页参数
     * @return 分页列表
     */
    @Operation(summary = "查询列表")
    @GetMapping("/list")
    public PageResult<?> list(PageModel pageModel) {
        List<FieldAreaEntity> list = fieldAreaService.list(pageModel);
        PageResult<?> pageResult = new PageResult<>(list);
        pageResult.setTotal(pageModel.getTotal());
        return pageResult;
    }

    /**
     * 查询单条信息
     *
     * @param id 主键id
     * @return 单条信息
     */
    @Operation(summary = "查询信息")
    @GetMapping("/info/{id}")
    public FieldAreaFormVO info(@PathVariable String id) {
        return fieldAreaService.info(id);
    }

    /**
     * 保存单条信息
     *
     * @param entity 实体
     */
    @Operation(summary = "保存")
    @PostMapping("/save")
    public void save(@RequestBody FieldAreaFormVO entity) {
        fieldAreaService.saveUpdate(entity);
    }

    /**
     * 批量删除信息
     *
     * @param ids 逗号分隔id
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public void delete(@RequestBody IdsModel ids) {
        fieldAreaService.delete(ids);
    }
}
