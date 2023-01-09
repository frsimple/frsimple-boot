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
import org.simple.dto.EnumVO;
import org.simple.dto.IdsModel;
import org.simple.dto.PageModel;
import org.simple.enums.system.IsDeletedEnum;
import org.simple.online.dto.datatype.DataTypeDetailVO;
import org.simple.online.dto.datatype.DataTypeVO;
import org.simple.online.entity.DataTypeDetailEntity;
import org.simple.online.entity.DataTypeEntity;
import org.simple.online.mapper.DataTypeMapper;
import org.simple.online.service.IDataTypeDetailService;
import org.simple.online.service.IDataTypeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yh_liu
 * @version v1.0
 * @since 2022-08-06
 */
@Service
@RequiredArgsConstructor
public class DataTypeServiceImpl extends ServiceImpl<DataTypeMapper, DataTypeEntity> implements IDataTypeService {

    private final IDataTypeDetailService dataTypeDetailService;

    /**
     * 查询列表
     *
     * @return 列表数据
     */
    @Override
    public List<DataTypeEntity> list(PageModel pageModel) {
        QueryWrapper<DataTypeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DataTypeEntity::getIsDeleted, IsDeletedEnum.False.getKey());
        if (StrUtil.isNotEmpty(pageModel.getSearchValue())) {
            queryWrapper.lambda().like(DataTypeEntity::getName, pageModel.getSearchValue()).or().like(DataTypeEntity::getCode, pageModel.getSearchValue());
        }
        if (StrUtil.isEmpty(pageModel.getSortField())) {
            queryWrapper.lambda().orderByAsc(DataTypeEntity::getSortIndex);
        }
        Page<DataTypeEntity> page = new Page<>(pageModel.getCurrent(), pageModel.getPageSize());
        IPage<DataTypeEntity> iPage = this.page(page, queryWrapper);
        return pageModel.setList(iPage.getRecords(), page.getTotal());
    }

    /**
     * 获取信息
     *
     * @param id 查询参数
     * @return 当前数据
     */
    @Override
    public DataTypeVO info(String id) {
        DataTypeEntity entity = this.getById(id);
        DataTypeVO vo = BeanUtil.toBean(entity, DataTypeVO.class);
        List<DataTypeDetailEntity> detailList = dataTypeDetailService.getDetailList(id);
        vo.setDetail(BeanUtil.copyToList(detailList, DataTypeDetailVO.class));
        return vo;
    }

    /**
     * 保存
     *
     * @param form 当前信息
     */
    @Override
    public void saveUpdate(DataTypeVO form) {
        DataTypeEntity entity = BeanUtil.toBean(form, DataTypeEntity.class);
        if (ObjectUtil.isEmpty(form.getId())) {
            entity.setId(String.valueOf(YitIdHelper.nextId()));
        }
        this.saveOrUpdate(entity);

        List<DataTypeDetailEntity> removeList = dataTypeDetailService.getList(entity.getId());
        if (form.getDetail().size() > 0) {
            List<DataTypeDetailEntity> detailList = BeanUtil.copyToList(form.getDetail(), DataTypeDetailEntity.class);
            List<String> inputIds = detailList.stream().filter(x -> ObjectUtil.isNotEmpty(x.getId())).map(x -> x.getId()).collect(Collectors.toList());
            List<DataTypeDetailEntity> existList = dataTypeDetailService.getDetailList(entity.getId());
            List<String> existIds = existList.stream().map(BaseEntity::getId).collect(Collectors.toList());
            String notExistIds = existIds.stream().filter(x -> !inputIds.contains(x)).collect(Collectors.joining(CommonConst.STRING_COMMA));
            removeList = dataTypeDetailService.getList(notExistIds);
            if (form.getDetail().size() > 0) {
                for (DataTypeDetailEntity item : detailList) {
                    if (ObjectUtil.isEmpty(item.getId())) {
                        item.setId(String.valueOf(YitIdHelper.nextId()));
                        item.setDetailId(entity.getId());
                    }
                }
                if (detailList.size() > 0) {
                    dataTypeDetailService.saveOrUpdateBatch(detailList);
                }
            }
            dataTypeDetailService.removeBatchByIds(removeList);
        }
        dataTypeDetailService.removeBatchByIds(removeList);
    }

    /**
     * 删除
     *
     * @param ids id数组
     */
    @Override
    public void delete(IdsModel ids) {
        List<DataTypeEntity> list = this.getListIds(ids.getId());
        List<DataTypeDetailEntity> detailList = dataTypeDetailService.getDetailList(ids.getId());
        this.removeBatchByIds(list);
        dataTypeDetailService.removeBatchByIds(detailList);
    }


    /**
     * 根据id获取列表
     *
     * @param ids 逗号分隔id
     * @return 列表
     */
    private List<DataTypeEntity> getListIds(String ids) {
        List<String> data = Arrays.asList(ids.split(CommonConst.STRING_COMMA));
        return this.listByIds(data);
    }

    /**
     * 获取数据类型枚举
     *
     * @return 枚举
     */
    @Override
    public List<EnumVO> getDataTypeEnum() {
        List<EnumVO> enumVOList = new ArrayList<>();
        QueryWrapper<DataTypeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DataTypeEntity::getIsDeleted, IsDeletedEnum.False.getKey());
        List<DataTypeEntity> dataTypeEntityList = this.list(queryWrapper);
        for (DataTypeEntity item : dataTypeEntityList) {
            List<DataTypeDetailEntity> detailList = dataTypeDetailService.getDetailList(item.getId());
            //此处暂时只支持“MySql”，我认为使用什么数据库应该写在全局变量之中
            DataTypeDetailEntity dataTypeDetail = detailList.stream().filter(x -> Objects.equals(x.getName(), "MySql")).findFirst().orElse(null);
            if (dataTypeDetail != null) {
                EnumVO enumVO = new EnumVO();
                enumVO.setKey(dataTypeDetail.getCode());
                enumVO.setValue(item.getName());
                enumVOList.add(enumVO);
            }
        }
        return enumVOList;
    }
}
