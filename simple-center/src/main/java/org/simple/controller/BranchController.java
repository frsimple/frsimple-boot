package org.simple.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.entity.Branch;
import org.simple.service.BranchService;
import org.simple.utils.CommonResult;
import org.simple.utils.RedomUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 组织管理控制器
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@AllArgsConstructor
@RequestMapping("/branch")
@Tag(name = "branch", description = "组织管理")
public class BranchController {
    private final BranchService branchService;

    @GetMapping("queryOrganTree")
    @Operation(summary = "查询组织机构树形列表")
    public CommonResult queryOrganTree(@RequestParam(required = false, name = "tenantId") String tenantId) {
        return CommonResult.success(branchService.queryOrganTree(tenantId));
    }


    @PostMapping("editOrgan")
    @Operation(summary = "修改组织机构信息")
    public CommonResult editOrgan(@RequestBody Branch branch) {
        return CommonResult.success(branchService.updateById(branch));
    }

    @PostMapping("addOrgan")
    @Operation(summary = "新增组织机构信息")
    public CommonResult addOrgan(@RequestBody Branch branch) {
        branch.setId(RedomUtil.getOrganId());
        branch.setCreatetime(LocalDateTime.now());
        return CommonResult.success(branchService.save(branch));
    }


    @DeleteMapping("delOrgan/{id}")
    @Operation(summary = "新增组织机构信息")
    public CommonResult delOrgan(@PathVariable("id") String id) {
        Branch branch = new Branch();
        branch.setParentid(id);
        if (branchService.list(Wrappers.query(branch)).size() != 0) {
            return CommonResult.failed("存在关联的下级组织，请先删除所有下级组织");
        }
        return CommonResult.success(branchService.removeById(id));
    }

    @DeleteMapping("getOrgan")
    @Operation(summary = "新增组织机构信息")
    public CommonResult getOrgan(@RequestParam("id") String id) {
        return CommonResult.success(branchService.getById(id));
    }
}