package org.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.simple.entity.Email;

/**
 * EmailMapper
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Mapper
public interface EmailMapper extends BaseMapper<Email> {
}
