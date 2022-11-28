package org.simple.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yitter.idgen.YitIdHelper;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.simple.dto.LogsDto;
import org.simple.entity.Logs;
import org.simple.mapper.LogsMapper;
import org.simple.service.IAuthService;
import org.simple.service.ILogsService;
import org.simple.service.IUserService;
import org.simple.utils.IpUtil;
import org.simple.utils.ServletUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * LogsServiceImpl
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Service
@AllArgsConstructor
public class LogsServiceImpl extends ServiceImpl<LogsMapper, Logs> implements ILogsService {

    @Override
    public IPage<List<LogsDto>> logsList(Page page, LogsDto logs) {
        return baseMapper.logsList(page, logs);
    }

    /**
     * 保存
     *
     * @param entity 当前信息
     * @return 是否成功
     */
    @Override
    public Boolean saveUpdate(Logs entity) {
        if (ObjectUtil.isEmpty(entity.getId())) {
            entity.setId(String.valueOf(YitIdHelper.nextId()));
        }
        return this.saveOrUpdate(entity);
    }


    /**
     * 根据请求转化日志实体
     *
     * @param serviceName 方法注释
     * @param duration    请求耗时
     * @param request     请求参数
     * @return 日志信息
     */
    @Override
    public Logs getLogData(String serviceName, String currentUserId, String duration, @NotNull HttpServletRequest request) {
        Logs logEntity = new Logs();
        logEntity.setId(String.valueOf(YitIdHelper.nextId()));
        logEntity.setServicename(serviceName);
        logEntity.setRecource("");
        logEntity.setIp(IpUtil.getIpAddress());
        logEntity.setPath(request.getRequestURI());
        logEntity.setMethod(request.getMethod());
        logEntity.setUseragent(ServletUtil.getUserAgent());
        logEntity.setParams(JSONUtil.toJsonStr(request.getParameterMap()));
        if (ObjectUtil.isNotNull(currentUserId)) {
            logEntity.setUsername(currentUserId);
        }
        logEntity.setCreatetime(DateTime.now().toLocalDateTime());
        logEntity.setTime(duration);
        logEntity.setStatus("0");
        return logEntity;
    }
}
