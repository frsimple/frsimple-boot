package org.simple.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.entity.Dictionary;
import org.simple.service.IDictionaryService;
import org.simple.utils.CommonResult;
import org.simple.utils.RandomUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据字典管理
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/center/dict")
@Tag(name = "dict", description = "字典管理")
public class DictController {
    private final IDictionaryService dictionaryService;

    @GetMapping("list")
    @Operation(summary = "查询字典")
    @SaCheckPermission(value = {"system:dict:query"}, mode = SaMode.OR)
    public CommonResult list(Page page, Dictionary dictionary) {
        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Dictionary::getValue, "#");
        String label = dictionary.getLabel();
        if (StrUtil.isNotEmpty(label)) {
            queryWrapper.and(x -> x.lambda().like(Dictionary::getLabel, label).or().like(Dictionary::getCode, label));
        }
        queryWrapper.lambda().orderByDesc(Dictionary::getCreatetime);
        return CommonResult.success(dictionaryService.page(page, queryWrapper));
    }

    @GetMapping("list1")
    @Operation(summary = "查询字典")
    @SaCheckPermission(value = {"system:dict:query"}, mode = SaMode.OR)
    public CommonResult list1(Dictionary dictionary) {
        String id = "";
        if (StrUtil.isNotEmpty(dictionary.getId())) {
            id = dictionary.getId();
        }
        dictionary.setId(null);
        dictionary.setValue("#");
        return CommonResult.success(dictionaryService.list(Wrappers.query(dictionary).
                notIn("id", id)));
    }

    @GetMapping("values")
    @Operation(summary = "查询字典项")
    @SaCheckPermission(value = {"system:dict:query"}, mode = SaMode.OR)
    public List<Dictionary> listValues(@RequestParam("code") String code) {
        Dictionary dictionary = new Dictionary();
        dictionary.setCode(code);
        return dictionaryService.list(
                Wrappers.query(dictionary).notIn("value", "#"));
    }

    @GetMapping("values/{code}")
    public List<Dictionary> listValues1(@PathVariable("code") String code) {
        Dictionary dictionary = new Dictionary();
        dictionary.setCode(code);
        return dictionaryService.list(
                Wrappers.query(dictionary).notIn("value", "#"));
    }

    @PostMapping("addDict")
    @Operation(summary = "新增字典")
    @SaCheckPermission(value = {"system:dict:add"}, mode = SaMode.OR)
    public Boolean addDict(@RequestBody Dictionary dictionary) {
        dictionary.setId(RandomUtil.getDictId());
        dictionary.setCreatetime(LocalDateTime.now());
        return dictionaryService.save(dictionary);
    }

    @PostMapping("editDict")
    @Operation(summary = "修改字典")
    @SaCheckPermission(value = {"system:dict:edit"}, mode = SaMode.OR)
    public Boolean editDict(@RequestBody Dictionary dictionary) {
        //清洗对象
        Dictionary d = dictionaryService.getById(dictionary.getId());
        if ("#".equals(d.getValue())) {
            Dictionary d1 = new Dictionary();
            d1.setCode(d.getCode());
            List<Dictionary> list = dictionaryService.
                    list(Wrappers.query(d1));
            if (CollectionUtil.isNotEmpty(list)) {
                list.forEach(dict -> {
                    dict.setCode(dictionary.getCode());
                    dictionaryService.updateById(dict);
                });
            }
            d.setLabel(dictionary.getLabel());
            d.setCode(dictionary.getCode());
        } else {
            d.setLabel(dictionary.getLabel());
            d.setValue(dictionary.getValue());
        }
        return dictionaryService.updateById(d);
    }

    @DeleteMapping("delDict")
    @Operation(summary = "删除字典")
    @SaCheckPermission(value = {"system:dict:del"}, mode = SaMode.OR)
    public Boolean delDict(@RequestParam("id") String id) {
        Dictionary d = dictionaryService.getById(id);
        if ("#".equals(d.getValue())) {
            Dictionary dic = new Dictionary();
            dic.setCode(d.getCode());
            return dictionaryService.remove(Wrappers.query(dic));
        } else {
            return dictionaryService.removeById(id);
        }
    }
}
