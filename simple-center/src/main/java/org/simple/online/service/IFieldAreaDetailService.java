package org.simple.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.online.entity.FieldAreaDetailEntity;

import java.util.List;

/**
 * @author ThePai
 * @version v1.0
 * @since 2022/8/7
 */
public interface IFieldAreaDetailService extends IService<FieldAreaDetailEntity> {
    /**
     * 根据主表id获取明细
     *
     * @param id 主表id
     * @return 子表信息
     */
    List<FieldAreaDetailEntity> getDetailList(String id);

    /**
     * 根据id获取明细
     *
     * @param id 明细表id
     * @return 子表信息
     */
    List<FieldAreaDetailEntity> getList(String id);
}
