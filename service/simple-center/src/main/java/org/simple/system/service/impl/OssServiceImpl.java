package org.simple.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.system.entity.OssEntity;
import org.simple.system.mapper.OssMapper;
import org.simple.system.service.IOssService;
import org.springframework.stereotype.Service;

/**
 * OssServiceImpl
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class OssServiceImpl extends ServiceImpl<OssMapper, OssEntity> implements IOssService {
}
