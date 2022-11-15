package org.simple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.entity.Dictionary;
import org.simple.mapper.DictionaryMapper;
import org.simple.service.DictionaryService;
import org.springframework.stereotype.Service;

/**
 * DictionaryServiceImpl
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

}
