package com.example.config;

import jakarta.servlet.ServletRegistration;
import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?> @Nullable [] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?> @Nullable [] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setLoadOnStartup(1);
        registration.setInitParameter("spring.profiles.active", "default");
    }
}
