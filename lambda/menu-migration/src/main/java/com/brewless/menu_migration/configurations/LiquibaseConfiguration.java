package com.brewless.menu_migration.configurations;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class LiquibaseConfiguration {

  @Bean
  public SpringLiquibase springLiquibase() {
    return new SpringLiquibase() {
      @Override
      public void afterPropertiesSet() {
        // no-op
      }
    };
  }
}