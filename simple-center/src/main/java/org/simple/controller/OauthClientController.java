package org.simple.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.simple.entity.OauthClientDetails;
import org.simple.service.IOauthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * 权限用户
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */

@RestController
@RequestMapping("/center/oauth")
@Tag(name = "oauth", description = "权限用户")
public class OauthClientController {

    @Autowired
    private IOauthClientService oauthClientService;

    @GetMapping("list")
    @Operation(summary = "查询客户端用户")
    public IPage<List<OauthClientDetails>> list(Page page, OauthClientDetails oauthClientDetails) {
        String clientId = oauthClientDetails.getClientId();
        oauthClientDetails.setClientId(null);
        return oauthClientService.page(page,
                Wrappers.query(oauthClientDetails).like("client_id", clientId));
    }

    @PostMapping("addClient")
    @Operation(summary = "新增客户端用户")
    public Boolean addClient(@RequestBody OauthClientDetails oauthClientDetails) {
        oauthClientDetails.setCreateTime(new Timestamp(System.currentTimeMillis()));
        oauthClientDetails.setArchived(0);
        oauthClientDetails.setTrusted(0);
        oauthClientDetails.setAutoapprove("false");
        return oauthClientService.save(oauthClientDetails);
    }

    @PostMapping("editClient")
    @Operation(summary = "修改客户端用户")
    public Boolean editClient(@RequestBody OauthClientDetails oauthClientDetails) {
        oauthClientDetails.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return oauthClientService.updateById(oauthClientDetails);
    }

    @DeleteMapping("delClient")
    @Operation(summary = "删除客户端用户")
    public Boolean delClient(@RequestParam("clientId") String clientId) {
        return oauthClientService.removeById(clientId);
    }

    @GetMapping("getClient/{id}")
    @Operation(summary = "根据id查询客户端用户")
    public OauthClientDetails getOne(@PathVariable("id") String clientId) {
        return oauthClientService.getById(clientId);
    }
}
