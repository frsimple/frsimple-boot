package org.simple.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.online.entity.DataTypeDetailEntity;

import java.util.List;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022-08-06
 */
public interface IDataTypeDetailService extends IService<DataTypeDetailEntity> {

    /**
     * 根据主表id获取明细
     *
     * @param id 主表id
     * @return 子表信息
     */
    List<DataTypeDetailEntity> getDetailList(String id);

    /**
     * 根据id获取明细
     *
     * @param id 明细表id
     * @return 子表信息
     */
    List<DataTypeDetailEntity> getList(String id);
}
