package org.simple.config.thread;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hw_ren
 * @version v1.0
 * @since 2022/8/9
 */
@Configuration
public class ThreadExecutorConfig {
    /**
     * 使用此定义线程池
     *
     * @return 线程执行
     */
    @Bean("threadExecutor")
    public Executor threadExecutor() {
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //保证至少有5个核心线程
        processors = Math.max(processors, 5);
        // 核心线程数
        executor.setCorePoolSize(processors);
        // 最大线程数
        executor.setMaxPoolSize(processors * 2);
        // 队列大小
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("threadExecutor-");
        // 线程最大空闲时间
        executor.setKeepAliveSeconds(20);
        // 拒绝策略 rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
