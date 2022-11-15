package org.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.simple.dto.LogsDto;
import org.simple.entity.Logs;

import java.util.List;

public interface LogsMapper extends BaseMapper<Logs> {

    IPage<List<LogsDto>>  logsList(Page page, @Param("obj")LogsDto logs);
}
