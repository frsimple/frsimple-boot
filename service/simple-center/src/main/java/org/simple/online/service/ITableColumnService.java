package org.simple.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.online.entity.TableColumnEntity;

import java.util.List;

/**
 * @author ThePai
 * @version v1.0
 * @since 2022-08-16
 */
public interface ITableColumnService extends IService<TableColumnEntity> {
    /**
     * 根据主表id获取明细
     *
     * @param id 主表id
     * @return 字段信息
     */
    List<TableColumnEntity> getDetailList(String id);

    /**
     * 根据id获取明细
     *
     * @param id 明细表id
     * @return 字段信息
     */
    List<TableColumnEntity> getList(String id);
}
