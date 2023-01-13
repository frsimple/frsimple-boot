package org.simple.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.system.dto.menu.MenuQuery;
import org.simple.system.dto.menu.MenuTreeDto;
import org.simple.system.entity.MenuEntity;

import java.util.List;

/**
 * MenuService
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
public interface IMenuService extends IService<MenuEntity> {
    List<MenuTreeDto> getTreeMenuAll();

    List<MenuTreeDto> getRoleMenuAll();

    Boolean delMenu(String id);

    Boolean delBtnMenu(String id);

    /**
     * 查询列表
     *
     * @param query 分页参数
     * @return 数据列表
     */
    List<MenuEntity> list(MenuQuery query);
}
