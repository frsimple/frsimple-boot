package org.simple.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.simple.online.entity.DataTypeEntity;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022/8/6
 */
@Mapper
public interface DataTypeMapper extends BaseMapper<DataTypeEntity> {
}