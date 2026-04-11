package com.example.listener;

import com.example.config.AppConfig;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SpringContextListener implements ServletContextListener {

    @Getter
    private static ApplicationContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
