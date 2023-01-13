package org.simple.system.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yitter.idgen.YitIdHelper;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.simple.system.dto.log.LogsQuery;
import org.simple.system.entity.LogsEntity;
import org.simple.system.entity.UserEntity;
import org.simple.enums.system.IsDeletedEnum;
import org.simple.system.mapper.LogsMapper;
import org.simple.system.service.ILogsService;
import org.simple.system.service.IUserService;
import org.simple.utils.IpUtil;
import org.simple.utils.PageUtil;
import org.simple.utils.ServletUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * LogsServiceImpl
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Service
@AllArgsConstructor
public class LogsServiceImpl extends ServiceImpl<LogsMapper, LogsEntity> implements ILogsService {

    private final IUserService userService;

    @Override
    public List<LogsEntity> logsList(LogsQuery query) {
        List<LogsEntity> logsList = baseMapper.logsList(query);
        return query.setList(PageUtil.getListPage(query.getCurrent(), query.getPageSize(), logsList), logsList.size());
    }

    /**
     * 保存
     *
     * @param entity 当前信息
     * @return 是否成功
     */
    @Override
    public Boolean saveUpdate(LogsEntity entity) {
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
    public LogsEntity getLogData(String serviceName, String currentUserId, String duration, @NotNull HttpServletRequest request) {
        LogsEntity logsEntity = new LogsEntity();
        logsEntity.setId(String.valueOf(YitIdHelper.nextId()));
        logsEntity.setServiceName(serviceName);
        logsEntity.setResource("");
        logsEntity.setIp(IpUtil.getIpAddress());
        logsEntity.setPath(request.getRequestURI());
        logsEntity.setMethod(request.getMethod());
        logsEntity.setUseragent(ServletUtil.getUserAgent());
        logsEntity.setParams(JSONUtil.toJsonStr(request.getParameterMap()));
        if (StrUtil.isNotEmpty(currentUserId)) {
            UserEntity userEntity = userService.getById(currentUserId);
            if (userEntity != null) {
                logsEntity.setUsername(userEntity.getId());
                logsEntity.setCreateUserId(userEntity.getId());
                logsEntity.setCreateUserName(userEntity.getNickName());
                logsEntity.setTenantId(userEntity.getTenant());
                logsEntity.setIsDeleted(IsDeletedEnum.True.getKey());
                logsEntity.setNickName(userEntity.getNickName());
            }
        }
        logsEntity.setTime(duration);
        logsEntity.setCreateTime(DateTime.now());
        logsEntity.setStatus("0");
        return logsEntity;
    }
}
