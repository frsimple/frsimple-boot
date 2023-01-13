package org.simple.config.snowflake;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

/**
 * SnowFlakeConfig
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/9
 */
@Configuration
@RequiredArgsConstructor
public class SnowFlakeConfig implements ApplicationRunner {

    private final SnowFlakeProperties snowFlakeProperties;

    /**
     * 自增雪花id注册配置
     *
     * @param args 参数
     */
    @Override
    public void run(ApplicationArguments args) {
        IdGeneratorOptions idGeneratorOptions = new IdGeneratorOptions(snowFlakeProperties.getWorkerId());
        YitIdHelper.setIdGenerator(idGeneratorOptions);
    }
}
