package com.ning.sdk;

import com.ning.sdk.client.MyClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ComponentScan
@ConfigurationProperties("api.client")
public class ApiClientSdkConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public MyClient myClient() {
        return new MyClient(accessKey, secretKey);
    }
}
