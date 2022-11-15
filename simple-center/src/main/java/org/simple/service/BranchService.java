package org.simple.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.entity.Branch;

import java.util.List;


/**
 * 组织管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public interface BranchService extends IService<Branch> {

    List<Tree<String>> queryOrganTree(String tenantName);

}