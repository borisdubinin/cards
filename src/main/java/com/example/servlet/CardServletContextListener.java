package com.example.servlet;

import com.example.config.DatabaseConfig;
import org.flywaydb.core.Flyway;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CardServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Flyway flyway = Flyway.configure()
                .dataSource(DatabaseConfig.getDataSource())
                .load();

        flyway.migrate();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseConfig.closeDataSource();
    }
}
