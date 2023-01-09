package org.simple.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.simple.online.entity.TableEntity;

/**
 * @author ThePai
 * @version v1.0
 * @since 2022/8/16
 */
@Mapper
public interface TableMapper extends BaseMapper<TableEntity> {
}
