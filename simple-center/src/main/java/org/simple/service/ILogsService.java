package org.simple.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.dto.LogsDto;
import org.simple.entity.Logs;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * LogsService
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
public interface ILogsService extends IService<Logs> {

    /**
     * 保存
     *
     * @param page 分页参数
     * @param logs 查询条件
     * @return 是否成功
     */
    IPage<List<LogsDto>> logsList(Page page, LogsDto logs);

    /**
     * 保存
     *
     * @param entity 当前信息
     * @return 是否成功
     */
    Boolean saveUpdate(Logs entity);

    /**
     * 根据请求转化日志实体
     *
     * @param serviceName   方法注释
     * @param currentUserId 当前人id
     * @param duration      请求耗时
     * @param request       请求参数
     * @return 日志信息
     */
    Logs getLogData(String serviceName, String currentUserId, String duration, HttpServletRequest request);
}
