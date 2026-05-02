package com.example.config;

import com.example.client.AccountClient;
import com.example.config.properties.FeignProperties;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor basicAuthRequestInterceptor(FeignProperties feignProperties) {
        return new BasicAuthRequestInterceptor(
                feignProperties.getUsername(),
                feignProperties.getPassword()
        );
    }

    @Bean
    public AccountClient accountClient(
            FeignProperties feignProperties,
            RequestInterceptor basicAuthRequestInterceptor) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(AccountClient.class))
                .logLevel(Logger.Level.FULL)
                .requestInterceptor(basicAuthRequestInterceptor)
                .target(AccountClient.class, feignProperties.getAccountsUrl());
    }
}