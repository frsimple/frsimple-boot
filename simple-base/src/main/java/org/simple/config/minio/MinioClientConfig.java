package org.simple.config.minio;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinioClientConfig
 *
 * @author yh_liu
 * @version v1.0
 * @since 2022/7/9
 */
@Configuration
@RequiredArgsConstructor
public class MinioClientConfig {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

}
