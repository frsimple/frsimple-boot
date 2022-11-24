package org.simple.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.entity.Menu;

import java.util.List;

/**
 * MenuService
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public interface IMenuService extends IService<Menu> {
    List<Tree<String>> getTreeMenuAll();

    List<Tree<String>> getRoleMenuAll();

    Boolean delMenu(String id);

    Boolean delBtnMenu(String id);
}
