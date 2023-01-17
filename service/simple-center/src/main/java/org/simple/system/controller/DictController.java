package org.simple.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yitter.idgen.YitIdHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.constant.CommonConst;
import org.simple.dto.IdsModel;
import org.simple.dto.PageResult;
import org.simple.system.dto.dict.DictQuery;
import org.simple.system.entity.DictionaryEntity;
import org.simple.system.service.IDictionaryService;
import org.simple.utils.CommonResult;
import org.simple.utils.RedisUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典管理
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/system/dict")
@Tag(name = "dict", description = "字典管理")
public class DictController {
    private final IDictionaryService dictionaryService;
    private final RedisUtil redisUtil;

    @GetMapping("list")
    @Operation(summary = "查询字典")
    @SaCheckPermission(value = {"system:dict:query"}, mode = SaMode.OR)
    public PageResult<DictionaryEntity> list(DictQuery query) {
        List<DictionaryEntity> list = dictionaryService.list(query);
        PageResult<DictionaryEntity> pageResult = new PageResult<>(list);
        pageResult.setTotal(query.getTotal());
        return pageResult;
    }

    @GetMapping("list1")
    @Operation(summary = "查询字典")
    @SaCheckPermission(value = {"system:dict:query"}, mode = SaMode.OR)
    public List<DictionaryEntity> list1(DictionaryEntity dictionaryEntity) {
        LambdaQueryWrapper<DictionaryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictionaryEntity::getCode, dictionaryEntity.getCode());
        queryWrapper.eq(DictionaryEntity::getValue, "#");
        queryWrapper.ne(DictionaryEntity::getId, dictionaryEntity.getId());
        return dictionaryService.list(queryWrapper);
    }

    @GetMapping("values")
    @Operation(summary = "查询字典项")
    @SaCheckPermission(value = {"system:dict:query"}, mode = SaMode.OR)
    public List<DictionaryEntity> listValues(@RequestParam("code") String code) {
        //先从缓存中拿
        Object data = redisUtil.get(code);
        if (ObjectUtil.isNotNull(data)) {
            List<DictionaryEntity> list = JSONUtil.toList(data.toString(), DictionaryEntity.class);
            return list;
        }
        LambdaQueryWrapper<DictionaryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictionaryEntity::getCode, code);
        queryWrapper.notIn(DictionaryEntity::getValue, "#");
        return dictionaryService.list(queryWrapper);
    }

    @GetMapping("values/{code}")
    public List<DictionaryEntity> listValues1(@PathVariable("code") String code) {
        //先从缓存中拿
        Object data = redisUtil.get(code);
        if (ObjectUtil.isNotNull(data)) {
            List<DictionaryEntity> list = JSONUtil.toList(data.toString(), DictionaryEntity.class);
            return list;
        }
        LambdaQueryWrapper<DictionaryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictionaryEntity::getCode, code);
        queryWrapper.notIn(DictionaryEntity::getValue, "#");
        return dictionaryService.list(queryWrapper);
    }

    @PostMapping("addDict")
    @Operation(summary = "新增字典")
    @SaCheckPermission(value = {"system:dict:add"}, mode = SaMode.OR)
    public CommonResult addDict(@RequestBody DictionaryEntity dictionaryEntity) {
        dictionaryEntity.setId(String.valueOf(YitIdHelper.nextId()));
        dictionaryService.save(dictionaryEntity);
        this.refDictCache();
        return CommonResult.success();
    }

    @PostMapping("editDict")
    @Operation(summary = "修改字典")
    @SaCheckPermission(value = {"system:dict:edit"}, mode = SaMode.OR)
    public CommonResult editDict(@RequestBody DictionaryEntity dictionaryEntity) {
        //清洗对象
        DictionaryEntity d = dictionaryService.getById(dictionaryEntity.getId());
        if (CommonConst.ALARM_SIGNAL.equals(d.getValue())) {
            DictionaryEntity d1 = new DictionaryEntity();
            d1.setCode(d.getCode());
            List<DictionaryEntity> list = dictionaryService.
                    list(Wrappers.query(d1));
            if (CollectionUtil.isNotEmpty(list)) {
                list.forEach(dict -> {
                    dict.setCode(dictionaryEntity.getCode());
                    dictionaryService.updateById(dict);
                });
            }
            d.setLabel(dictionaryEntity.getLabel());
            d.setCode(dictionaryEntity.getCode());
        } else {
            d.setLabel(dictionaryEntity.getLabel());
            d.setValue(dictionaryEntity.getValue());
        }
        dictionaryService.updateById(d);
        this.refDictCache();
        return CommonResult.success();
    }

    @PostMapping("delDict")
    @Operation(summary = "删除字典")
    @SaCheckPermission(value = {"system:dict:del"}, mode = SaMode.OR)
    public CommonResult delDict(@RequestBody IdsModel model) {
        DictionaryEntity d = dictionaryService.getById(model.getId());
        if (CommonConst.ALARM_SIGNAL.equals(d.getValue())) {
            DictionaryEntity dic = new DictionaryEntity();
            dic.setCode(d.getCode());
            dictionaryService.remove(Wrappers.query(dic));
        } else {
            dictionaryService.removeById(model.getId());
        }
        this.refDictCache();
        return CommonResult.success();
    }

    @GetMapping("refDictCache")
    @Operation(summary = "刷新字段缓存")
    @SaCheckPermission(value = {"system:dict:query"}, mode = SaMode.OR)
    public CommonResult refDictCache() {
        DictionaryEntity dictionaryEntity = new DictionaryEntity();
        dictionaryEntity.setValue("#");
        List<DictionaryEntity> dictionaryEntityList = dictionaryService.list(Wrappers.query(dictionaryEntity));
        if (dictionaryEntityList.size() != 0) {
            for (DictionaryEntity item : dictionaryEntityList) {
                DictionaryEntity d = new DictionaryEntity();
                d.setCode(item.getCode());
                List<DictionaryEntity> list =
                        dictionaryService.list(Wrappers.query(d).notIn("value", "#"));
                JSONArray array = new JSONArray();
                if (list.size() != 0) {
                    for (DictionaryEntity item1 : list) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("value", item1.getValue());
                        jsonObject.put("id", item1.getId());
                        jsonObject.put("label", item1.getLabel());
                        jsonObject.put("code", item1.getCode());
                        array.add(jsonObject);
                    }
                }
                redisUtil.set(item.getCode(), String.valueOf(array));
            }
        }
        return CommonResult.success();
    }
}
