package org.simple.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.simple.entity.Dictionary;
import org.simple.service.DictionaryService;
import org.simple.utils.RedomUtil;
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
@RequestMapping("/dict")
@Tag(name = "dict", description = "字典管理")
public class DictController {
    private final DictionaryService dictionaryService;

    @GetMapping("list")
    @Operation(summary = "查询字典")
    public IPage<List<Dictionary>> list(Page page, Dictionary dictionary) {
        //只查询字典
        dictionary.setValue("#");
        String label = "";
        if (StrUtil.isNotEmpty(dictionary.getLabel())) {
            label = dictionary.getLabel();
            dictionary.setLabel("");
        }
        return dictionaryService.page(page,
                Wrappers.query(dictionary).like("label", label)
                        .or().like("code", label)
                        .orderByDesc("createtime"));
    }

    @GetMapping("list1")
    @Operation(summary = "查询字典")
    public List<Dictionary> list1(Dictionary dictionary) {
        String id = "";
        if (StrUtil.isNotEmpty(dictionary.getId())) {
            id = dictionary.getId();
        }
        dictionary.setId(null);
        dictionary.setValue("#");
        return dictionaryService.list(Wrappers.query(dictionary).
                notIn("id", id));
    }

    @GetMapping("values")
    @Operation(summary = "查询字典项")
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
    public Boolean addDict(@RequestBody Dictionary dictionary) {
        dictionary.setId(RedomUtil.getDictId());
        dictionary.setCreatetime(LocalDateTime.now());
        return dictionaryService.save(dictionary);
    }

    @PostMapping("editDict")
    @Operation(summary = "修改字典")
    public Boolean editDict(@RequestBody Dictionary dictionary) {
        //清洗对象
        Dictionary d = dictionaryService.getById(dictionary.getId());
        if (d.getValue().equals("#")) {
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
            return dictionaryService.updateById(d);
        } else {
            d.setLabel(dictionary.getLabel());
            d.setValue(dictionary.getValue());
            return dictionaryService.updateById(d);
        }
    }

    @DeleteMapping("delDict")
    @Operation(summary = "删除字典")
    public Boolean delDict(@RequestParam("id") String id) {
        Dictionary d = dictionaryService.getById(id);
        if (d.getValue().equals("#")) {
            Dictionary dic = new Dictionary();
            dic.setCode(d.getCode());
            return dictionaryService.remove(Wrappers.query(dic));
        } else {
            return dictionaryService.removeById(id);
        }
    }
}
