package com.example.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FeignProperties {

    @Value("${feign.url}")
    private String feignUrl;
}
