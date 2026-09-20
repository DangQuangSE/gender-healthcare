package com.S_Health.GenderHealthCare.integrations.payos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class PayOSClientConfiguration {

    @Bean
    RestClient payOSRestClient(RestClient.Builder builder, PayOSConfig config) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(config.getConnectTimeoutSeconds()))
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(config.getReadTimeoutSeconds()));

        return builder
                .baseUrl(config.getApiBaseUrl())
                .requestFactory(requestFactory)
                .build();
    }
}
