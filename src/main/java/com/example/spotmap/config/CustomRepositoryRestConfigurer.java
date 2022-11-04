package com.example.spotmap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;
//change configuration of spring spring-boot-starter-data-rest because by default it not includes id's.

@Configuration
public class CustomRepositoryRestConfigurer implements RepositoryRestConfigurer {

    @Autowired
    private EntityManager entityManager;
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // add id to all json entities
        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));
        cors.addMapping("/**")
                .allowedOrigins(CorsConfig.ALLOWED_ORIGINS);
    }
}