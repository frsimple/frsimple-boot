package org.simple.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.dto.OrganTreeDto;
import org.simple.entity.Branch;
import org.simple.mapper.BranchMapper;
import org.simple.service.IBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BranchServiceImpl
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class BranchServiceImpl extends ServiceImpl<BranchMapper, Branch> implements IBranchService {

    @Autowired
    private BranchMapper branchMapper;

    @Override
    public List<Tree<String>> queryTree(String tenantId) {
        List<OrganTreeDto> treeDtoList;
        if (StrUtil.isEmpty(tenantId)) {
            treeDtoList = branchMapper.queryOrganTree();
        } else {
            treeDtoList = branchMapper.queryOrganTreeByName(tenantId);
        }
        TreeNodeConfig config = new TreeNodeConfig();
        config.setWeightKey("sort");
        List<Tree<String>> treeNodes = TreeUtil.build(treeDtoList, "0", config,
                (object, tree) -> {
                    tree.setName(object.getName());
                    tree.setId(object.getId());
                    tree.setParentId(object.getParentid());
                    tree.setWeight(StrUtil.isEmpty(object.getSort()) ? 0 : Integer.valueOf(object.getSort()));
                    tree.putExtra("createtime", object.getCreatetime());
                    tree.putExtra("tenantname", object.getTenantname());
                    tree.putExtra("tenantid", object.getTenantid());
                    tree.putExtra("parentname", object.getParentname());
                });
        return treeNodes;
    }
}