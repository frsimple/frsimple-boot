package org.simple.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.dto.EnumVO;
import org.simple.dto.IdsModel;
import org.simple.dto.PageModel;
import org.simple.online.dto.datatype.DataTypeVO;
import org.simple.online.entity.DataTypeEntity;

import java.util.List;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022-08-06
 */
public interface IDataTypeService extends IService<DataTypeEntity> {

    /**
     * 查询所有列表
     *
     * @param pageModel 查询参数
     * @return 列表数据
     */
    List<DataTypeEntity> list(PageModel pageModel);

    /**
     * 新增
     *
     * @param id 主键id
     * @return 是否成功
     */
    DataTypeVO info(String id);

    /**
     * 保存
     *
     * @param entity 信息
     */
    void saveUpdate(DataTypeVO entity);

    /**
     * 删除
     *
     * @param ids id数组
     */
    void delete(IdsModel ids);

    /**
     * 获取数据类型枚举
     *
     * @return 枚举
     */
    List<EnumVO> getDataTypeEnum();
}
