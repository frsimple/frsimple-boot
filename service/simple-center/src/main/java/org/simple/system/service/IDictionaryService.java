package org.simple.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.simple.system.dto.dict.DictQuery;
import org.simple.system.entity.DictionaryEntity;

import java.util.List;

/**
 * DictionaryService
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
public interface IDictionaryService extends IService<DictionaryEntity> {

    List<DictionaryEntity> list(DictQuery query);
}
