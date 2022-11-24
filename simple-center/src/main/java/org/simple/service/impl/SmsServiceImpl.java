package org.simple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.entity.Sms;
import org.simple.mapper.SmsMapper;
import org.simple.service.ISmsService;
import org.springframework.stereotype.Service;

/**
 * SmsServiceImpl
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class SmsServiceImpl extends ServiceImpl<SmsMapper, Sms> implements ISmsService {
}
