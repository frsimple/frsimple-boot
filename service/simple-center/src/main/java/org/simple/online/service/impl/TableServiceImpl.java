package org.simple.online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yitter.idgen.YitIdHelper;
import lombok.RequiredArgsConstructor;
import org.simple.constant.CommonConst;
import org.simple.dto.BaseEntity;
import org.simple.dto.IdsModel;
import org.simple.dto.PageModel;
import org.simple.online.dto.table.TableColumnVO;
import org.simple.online.dto.table.TableVO;
import org.simple.online.entity.TableColumnEntity;
import org.simple.online.entity.TableEntity;
import org.simple.online.mapper.TableMapper;
import org.simple.online.service.ITableColumnService;
import org.simple.online.service.ITableService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022-08-06
 */
@Service
@RequiredArgsConstructor
public class TableServiceImpl extends ServiceImpl<TableMapper, TableEntity> implements ITableService {

    private final ITableColumnService tableColumnService;

    /**
     * 查询列表
     *
     * @param pageModel 查询参数
     * @return 列表数据
     */
    @Override
    public List<TableEntity> list(PageModel pageModel) {
        QueryWrapper<TableEntity> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(pageModel.getSearchValue())) {
            queryWrapper.lambda().like(TableEntity::getTableName, pageModel.getSearchValue()).or().like(TableEntity::getCode, pageModel.getSearchValue());
        }
        if (StrUtil.isEmpty(pageModel.getSortField())) {
            queryWrapper.lambda().orderByDesc(TableEntity::getCreateTime);
        }
        Page<TableEntity> page = new Page<>(pageModel.getCurrent(), pageModel.getPageSize());
        IPage<TableEntity> iPage = this.page(page, queryWrapper);
        return pageModel.setList(iPage.getRecords(), page.getTotal());
    }

    /**
     * 获取信息
     *
     * @param id 主键id
     * @return 当前数据
     */
    @Override
    public TableVO info(String id) {
        TableEntity entity = this.getById(id);
        TableVO vo = BeanUtil.toBean(entity, TableVO.class);
        List<TableColumnEntity> detailList = tableColumnService.getDetailList(id);
        vo.setColumn(BeanUtil.copyToList(detailList, TableColumnVO.class));
        return vo;
    }

    /**
     * 保存
     *
     * @param form 当前信息
     */
    @Override
    public void saveUpdate(TableVO form) {
        TableEntity entity = BeanUtil.toBean(form, TableEntity.class);
        if (ObjectUtil.isEmpty(form.getId())) {
            entity.setId(String.valueOf(YitIdHelper.nextId()));
        }
        this.saveOrUpdate(entity);

        List<TableColumnEntity> removeList = tableColumnService.getList(entity.getId());
        if (form.getColumn().size() > 0) {
            List<TableColumnEntity> detailList = BeanUtil.copyToList(form.getColumn(), TableColumnEntity.class);
            List<String> inputIds = detailList.stream().map(BaseEntity::getId).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
            List<TableColumnEntity> existList = tableColumnService.getDetailList(entity.getId());
            List<String> existIds = existList.stream().map(BaseEntity::getId).collect(Collectors.toList());
            String notExistIds = existIds.stream().filter(x -> !inputIds.contains(x)).collect(Collectors.joining(CommonConst.STRING_COMMA));
            removeList = tableColumnService.getList(notExistIds);
            if (form.getColumn().size() > 0) {
                for (TableColumnEntity item : detailList) {
                    if (ObjectUtil.isEmpty(item.getId())) {
                        item.setId(String.valueOf(YitIdHelper.nextId()));
                        item.setTableId(entity.getId());
                    }
                }
                if (detailList.size() > 0) {
                    tableColumnService.saveOrUpdateBatch(detailList);
                }
            }
            tableColumnService.removeBatchByIds(removeList);
        }
        tableColumnService.removeBatchByIds(removeList);
    }

    /**
     * 删除
     *
     * @param ids id数组
     */
    @Override
    public void delete(IdsModel ids) {
        List<TableEntity> list = this.getListIds(ids.getId());
        List<TableColumnEntity> detailList = tableColumnService.getDetailList(ids.getId());
        this.removeBatchByIds(list);
        tableColumnService.removeBatchByIds(detailList);
    }


    /**
     * 根据id获取列表
     *
     * @param ids 逗号分隔id
     * @return 列表
     */
    private List<TableEntity> getListIds(String ids) {
        List<String> data = Arrays.asList(ids.split(CommonConst.STRING_COMMA));
        return this.listByIds(data);
    }

}
