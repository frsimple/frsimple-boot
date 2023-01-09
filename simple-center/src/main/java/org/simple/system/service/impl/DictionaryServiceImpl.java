package org.simple.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.system.dto.dict.DictQuery;
import org.simple.system.entity.DictionaryEntity;
import org.simple.system.mapper.DictionaryMapper;
import org.simple.system.service.IDictionaryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DictionaryServiceImpl
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, DictionaryEntity> implements IDictionaryService {

    @Override
    public List<DictionaryEntity> list(DictQuery query) {
        LambdaQueryWrapper<DictionaryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictionaryEntity::getValue, "#");
        if (StrUtil.isNotEmpty(query.getLabel())) {
            queryWrapper.and(x -> x.like(DictionaryEntity::getLabel, query.getLabel()).or().like(DictionaryEntity::getCode, query.getLabel()));
        }

        Page<DictionaryEntity> page = new Page<>(query.getCurrent(), query.getPageSize());
        IPage<DictionaryEntity> iPage = this.page(page, queryWrapper);
        return query.setList(iPage.getRecords(), page.getTotal());
    }
}
