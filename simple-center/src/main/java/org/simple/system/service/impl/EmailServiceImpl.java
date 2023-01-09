package org.simple.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.system.entity.EmailEntity;
import org.simple.system.mapper.EmailMapper;
import org.simple.system.service.IEmailService;
import org.springframework.stereotype.Service;

/**
 * EmailServiceImpl
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class EmailServiceImpl extends ServiceImpl<EmailMapper, EmailEntity> implements IEmailService {
}
