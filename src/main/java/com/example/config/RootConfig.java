package com.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
        "com.example.config.properties",
        "com.example.converter",
        "com.example.service",
        "com.example.repository"
})
@Import({DatabaseConfig.class, PropertiesConfig.class})
public class RootConfig {
}
