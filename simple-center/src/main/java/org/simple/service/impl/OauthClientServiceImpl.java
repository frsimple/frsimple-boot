package org.simple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.entity.OauthClientDetails;
import org.simple.mapper.OauthClientMapper;
import org.simple.service.IOauthClientService;
import org.springframework.stereotype.Service;

/**
 * OauthClientServiceImpl
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class OauthClientServiceImpl extends ServiceImpl<OauthClientMapper, OauthClientDetails> implements IOauthClientService {
}
