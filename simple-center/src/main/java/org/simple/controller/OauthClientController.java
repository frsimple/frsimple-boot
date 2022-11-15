package org.simple.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.entity.OauthClientDetails;
import org.simple.service.OauthClientService;
import org.simple.utils.CommonResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

/**
 * 权限用户
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@AllArgsConstructor
@RequestMapping("/oauth")
@Tag(name = "oauth", description = "权限用户")
public class OauthClientController {
    private final OauthClientService oauthClientService;

    @GetMapping("list")
    @Operation(summary = "查询客户端用户")
    public CommonResult list(Page page, OauthClientDetails oauthClientDetails) {
        String clientId = oauthClientDetails.getClientId();
        oauthClientDetails.setClientId(null);
        return CommonResult.success(oauthClientService.page(page,
                Wrappers.query(oauthClientDetails).like("client_id", clientId)));
    }

    @PostMapping("addClient")
    @Operation(summary = "新增客户端用户")
    public CommonResult addClient(@RequestBody OauthClientDetails oauthClientDetails) {
        oauthClientDetails.setCreateTime(new Timestamp(System.currentTimeMillis()));
        oauthClientDetails.setArchived(0);
        oauthClientDetails.setTrusted(0);
        oauthClientDetails.setAutoapprove("false");
        return CommonResult.success(oauthClientService.save(oauthClientDetails));
    }

    @PostMapping("editClient")
    @Operation(summary = "修改客户端用户")
    public CommonResult editClient(@RequestBody OauthClientDetails oauthClientDetails) {
        oauthClientDetails.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return CommonResult.success(oauthClientService.updateById(oauthClientDetails));
    }

    @DeleteMapping("delClient")
    @Operation(summary = "删除客户端用户")
    public CommonResult delClient(@RequestParam("clientId") String clientId) {
        return CommonResult.success(oauthClientService.removeById(clientId));
    }

    @GetMapping("getClient/{id}")
    @Operation(summary = "根据id查询客户端用户")
    public CommonResult getOne(@PathVariable("id") String clientId) {
        return CommonResult.success(oauthClientService.getById(clientId));
    }

}
