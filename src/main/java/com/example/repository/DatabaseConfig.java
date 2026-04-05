package com.example.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final String CONFIG_FILE = "db/config/database_connection.properties";
    private static HikariDataSource dataSource;

    static {
        try {
            initializeDataSource();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize database configuration", e);
        }
    }

    public static HikariDataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource not initialized");
        }
        return dataSource;
    }

    private static void initializeDataSource() throws IOException {
        Properties props = loadProperties();

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.user"));
        config.setPassword(props.getProperty("db.password"));

        dataSource = new HikariDataSource(config);
    }

    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("Configuration file not found: " + CONFIG_FILE);
            }
            props.load(input);
        }
        return props;
    }
}
