package org.simple.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.simple.system.entity.EmailEntity;

/**
 * EmailMapper
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Mapper
public interface EmailMapper extends BaseMapper<EmailEntity> {
}
