package org.simple.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.center.entity.Menu;
import org.simple.common.utils.CommonResult;
import org.simple.entity.Menu;
import org.simple.utils.CommonResult;

import java.util.List;

public interface MenuService  extends IService<Menu> {
    List<Tree<String>> getTreeMenuAll();

    List<Tree<String>> getRoleMenuAll();

    CommonResult delMenu(String id);

    CommonResult delBtnMenu(String id);
}
