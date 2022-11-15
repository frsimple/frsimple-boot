package org.simple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.entity.OauthClientDetails;
import org.simple.mapper.OauthClientMapper;
import org.simple.service.OauthClientService;
import org.springframework.stereotype.Service;

@Service
public class OauthClientServiceImpl extends ServiceImpl<OauthClientMapper, OauthClientDetails> implements OauthClientService {
}
