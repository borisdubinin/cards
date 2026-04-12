package com.example.config;

import com.example.converter.CardConverter;
import com.example.repository.CardRepository;
import com.example.repository.DataBaseCardRepository;
import com.example.service.CardService;
import com.example.service.CardServiceImpl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan("com.example.servlet")
@EnableWebMvc
public class AppConfig {

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
    public CardRepository cardRepository(DataSource dataSource) {
        return new DataBaseCardRepository(dataSource);
    }

    @Bean
    public CardService cardService(CardRepository cardRepository) {
        return new CardServiceImpl(cardRepository);
    }

    @Bean
    public CardConverter cardConverter() {
        return new CardConverter();
    }
}
