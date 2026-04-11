package com.example.config;

import com.example.converter.CardConverter;
import com.example.repository.CardRepository;
import com.example.repository.DataBaseCardRepository;
import com.example.service.CardService;
import com.example.service.CardServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return DatabaseConfig.getDataSource();
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
