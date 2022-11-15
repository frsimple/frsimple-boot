package org.simple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.entity.Sms;
import org.simple.mapper.SmsMapper;
import org.simple.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl extends ServiceImpl<SmsMapper, Sms> implements SmsService {
}
