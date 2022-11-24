package org.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.simple.entity.OauthClientDetails;

/**
 * OauthClientMapper
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Mapper
public interface OauthClientMapper extends BaseMapper<OauthClientDetails> {
}
