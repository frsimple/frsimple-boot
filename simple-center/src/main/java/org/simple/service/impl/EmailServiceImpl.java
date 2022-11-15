package org.simple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.entity.Email;
import org.simple.mapper.EmailMapper;
import org.simple.service.EmailService;
import org.springframework.stereotype.Service;

/**
 * @author 22699
 */
@Service
public class EmailServiceImpl extends ServiceImpl<EmailMapper, Email> implements EmailService {
}
