package org.simple;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * springboot启动类
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022-7-7
 */
@EnableAsync
@MapperScan("org.simple.mapper")
@SpringBootApplication(scanBasePackages = "org.simple")
public class SimpleApplication {
    /**
     * springBoot入口方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SimpleApplication.class, args);
        System.out.println("启动完成");
    }
}
