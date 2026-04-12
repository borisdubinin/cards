package com.example.config;

import com.example.converter.CardConverter;
import com.example.repository.CardRepository;
import com.example.service.CardService;
import com.example.service.CardServiceImpl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan("com.example")
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public Properties databaseProperties() throws IOException {
        Properties props = new Properties();
        String configFile = "db/config/database.properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new IOException("Configuration file not found: %s".formatted(configFile));
            }
            props.load(input);
        }
        return props;
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource(@Qualifier("databaseProperties") Properties databaseProperties) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(databaseProperties.getProperty("db.url"));
        config.setUsername(databaseProperties.getProperty("db.user"));
        config.setPassword(databaseProperties.getProperty("db.password"));
        return new HikariDataSource(config);
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .load();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public CardService cardService(CardRepository cardRepository) {
        return new CardServiceImpl(cardRepository);
    }

    @Bean
    public CardConverter cardConverter() {
        return new CardConverter();
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/");
    }
}
