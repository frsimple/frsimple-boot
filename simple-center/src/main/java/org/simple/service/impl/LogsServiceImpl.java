package org.simple.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yitter.idgen.YitIdHelper;
import lombok.AllArgsConstructor;
import org.simple.constant.CommonConst;
import org.simple.dto.LogsDto;
import org.simple.entity.Logs;
import org.simple.entity.User;
import org.simple.mapper.LogsMapper;
import org.simple.service.IAuthService;
import org.simple.service.ILogsService;
import org.simple.service.IUserService;
import org.simple.utils.IpUtil;
import org.simple.utils.ServletUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    private final IUserService userService;
    private final IAuthService authService;

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
     * 根据id获取列表
     *
     * @param ids 逗号分隔id
     * @return 列表
     */
    private List<Logs> getListIds(String ids) {
        List<String> data = Arrays.asList(ids.split(CommonConst.STRING_COMMA));
        return this.listByIds(data);
    }
}
