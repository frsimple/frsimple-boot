package org.simple.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yitter.idgen.YitIdHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.dto.PageModel;
import org.simple.enums.system.ResultCodeEnum;
import org.simple.exception.CustomException;
import org.simple.system.dto.organ.OrganTreeDto;
import org.simple.system.entity.OrganEntity;
import org.simple.system.service.IOrganService;
import org.simple.utils.CommonResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织管理控制器
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/system/organ")
@Tag(name = "organ", description = "组织管理")
public class OrganController {
    private final IOrganService organService;

    @GetMapping("/queryOrganTree")
    @Operation(summary = "查询组织机构树形列表")
    @SaCheckPermission(value = {"system:organ:query"}, mode = SaMode.OR)
    public List<OrganTreeDto> queryOrganTree(PageModel pageModel) {
        return organService.queryTree(pageModel);
    }

    @PostMapping("/editOrgan")
    @Operation(summary = "修改组织机构信息")
    @SaCheckPermission(value = {"system:organ:edit"}, mode = SaMode.OR)
    public CommonResult editOrgan(@RequestBody OrganEntity organEntity) {
        organService.updateById(organEntity);
        return CommonResult.success();
    }

    @PostMapping("addOrgan")
    @Operation(summary = "新增组织机构信息")
    @SaCheckPermission(value = {"system:organ:add"}, mode = SaMode.OR)
    public CommonResult addOrgan(@RequestBody OrganEntity organEntity) {
        organEntity.setId(String.valueOf(YitIdHelper.nextId()));
        organService.save(organEntity);
        return CommonResult.success();
    }


    @PostMapping("delOrgan/{id}")
    @Operation(summary = "新增组织机构信息")
    @SaCheckPermission(value = {"system:organ:del"}, mode = SaMode.OR)
    public CommonResult delOrgan(@PathVariable("id") Long id) throws CustomException {
        OrganEntity organEntity = new OrganEntity();
        organEntity.setParentId(id);
        if (organService.list(Wrappers.query(organEntity)).size() != 0) {
            throw new CustomException(ResultCodeEnum.DB5001.getCode());
        }
        organService.removeById(id);
        return CommonResult.success();
    }

    @GetMapping("getOrgan/{id}")
    @Operation(summary = "查询组织机构信息")
    @SaCheckPermission(value = {"system:organ:query"}, mode = SaMode.OR)
    public CommonResult getOrgan(@PathVariable("id") String id) {
        return CommonResult.success(organService.getById(id));
    }
}