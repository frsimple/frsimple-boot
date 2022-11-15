package org.simple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.simple.entity.Oss;
import org.simple.mapper.OssMapper;
import org.simple.service.OssService;
import org.springframework.stereotype.Service;

/**
 * OssServiceImpl
 *
 * @author frsimple
 * @version v1.0
 * @since 2022/11/13
 */
@Service
public class OssServiceImpl extends ServiceImpl<OssMapper, Oss> implements OssService {
}
