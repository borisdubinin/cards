package com.example.initializer;

import com.example.config.DatabaseConfig;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
//@WebListener
public class FlywayMigrationListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(FlywayMigrationListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(DatabaseConfig.getDataSource())
                    .load();

            flyway.migrate();
            logger.log(Level.INFO, "Migrations have started");

        } catch (FlywayException e) {
            logger.log(Level.SEVERE, "Errors occurred during migrations. Continuing is not possible.");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseConfig.closeDataSource();
    }
}
