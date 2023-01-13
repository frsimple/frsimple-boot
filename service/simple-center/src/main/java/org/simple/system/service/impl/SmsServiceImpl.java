package org.simple.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.system.entity.SmsEntity;
import org.simple.system.mapper.SmsMapper;
import org.simple.system.service.ISmsService;
import org.springframework.stereotype.Service;

/**
 * SmsServiceImpl
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class SmsServiceImpl extends ServiceImpl<SmsMapper, SmsEntity> implements ISmsService {
}
