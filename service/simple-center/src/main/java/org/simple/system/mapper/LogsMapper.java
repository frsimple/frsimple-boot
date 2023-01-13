package org.simple.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.simple.system.dto.log.LogsQuery;
import org.simple.system.entity.LogsEntity;

import java.util.List;

/**
 * LogsMapper
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Mapper
public interface LogsMapper extends BaseMapper<LogsEntity> {
    List<LogsEntity> logsList(@Param("obj") LogsQuery logs);
}
