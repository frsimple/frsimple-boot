package org.simple.controller;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.entity.Branch;
import org.simple.enums.system.ResultCode;
import org.simple.exception.CustomException;
import org.simple.service.IBranchService;
import org.simple.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 组织管理控制器
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/branch")
@Tag(name = "branch", description = "组织管理")
public class BranchController {
    private final IBranchService branchService;

    @GetMapping("/queryOrganTree")
    @Operation(summary = "查询组织机构树形列表")
    public List<Tree<String>> queryOrganTree(@RequestParam(required = false, name = "tenantId") String tenantId) {
        return branchService.queryTree(tenantId);
    }


    @PostMapping("/editOrgan")
    @Operation(summary = "修改组织机构信息")
    public Boolean editOrgan(@RequestBody Branch branch) {
        return branchService.updateById(branch);
    }

    @PostMapping("addOrgan")
    @Operation(summary = "新增组织机构信息")
    public Boolean addOrgan(@RequestBody Branch branch) {
        branch.setId(RandomUtil.getOrganId());
        branch.setCreatetime(LocalDateTime.now());
        return branchService.save(branch);
    }


    @DeleteMapping("delOrgan/{id}")
    @Operation(summary = "新增组织机构信息")
    public Boolean delOrgan(@PathVariable("id") String id) throws CustomException {
        Branch branch = new Branch();
        branch.setParentid(id);
        if (branchService.list(Wrappers.query(branch)).size() != 0) {
            throw new CustomException(ResultCode.DB001.getCode());
        }
        return branchService.removeById(id);
    }

    @DeleteMapping("getOrgan")
    @Operation(summary = "新增组织机构信息")
    public Branch getOrgan(@RequestParam("id") String id) {
        return branchService.getById(id);
    }
}