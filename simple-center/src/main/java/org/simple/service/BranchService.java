package org.simple.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.center.entity.Branch;
import org.simple.entity.Branch;

import java.util.List;


/**
 * @Copyright: simple
 * @Desc: <br/>
 * @Date: 2022-08-03 21:47:58
 * @Author: frsimple
 */
public interface BranchService extends IService<Branch> {

    public List<Tree<String>> queryOrganTree(String tenantName);

}