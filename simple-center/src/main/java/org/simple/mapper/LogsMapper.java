package org.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.simple.dto.LogsDto;
import org.simple.entity.Logs;

import java.util.List;

/**
 * LogsMapper
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Mapper
public interface LogsMapper extends BaseMapper<Logs> {
    IPage<List<LogsDto>> logsList(Page page, @Param("obj") LogsDto logs);
}
