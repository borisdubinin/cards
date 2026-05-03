package com.example.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FeignProperties {

    @Value("${feign.accounts.url}")
    private String accountsUrl;

    @Value("${feign.accounts.username}")
    private String accountsUsername;

    @Value("${feign.accounts.password}")
    private String accountsPassword;
}
