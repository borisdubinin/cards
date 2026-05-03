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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean(name = "accountsRequestInterceptor")
    public RequestInterceptor accountsAuthRequestInterceptor(FeignProperties feignProperties) {
        return new BasicAuthRequestInterceptor(
                feignProperties.getAccountsUsername(),
                feignProperties.getAccountsPassword()
        );
    }

    @Bean
    public AccountClient accountClient(
            FeignProperties feignProperties,
            @Qualifier(value = "accountsRequestInterceptor") RequestInterceptor requestInterceptor) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(AccountClient.class))
                .logLevel(Logger.Level.FULL)
                .requestInterceptor(requestInterceptor)
                .target(AccountClient.class, feignProperties.getAccountsUrl());
    }
}