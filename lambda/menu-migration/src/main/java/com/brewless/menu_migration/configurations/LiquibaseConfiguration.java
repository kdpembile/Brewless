package com.brewless.menu_migration.configurations;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.integration.spring.SpringResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class LiquibaseConfiguration implements InitializingBean {

  private final LiquibaseProperties properties;

  private final ResourceLoader resourceLoader;

  @Override
  public void afterPropertiesSet() throws Exception {
    Database openDatabase = DatabaseFactory
        .getInstance()
        .openDatabase(properties.getUrl(), null, null, null, null, null);

    try (
      Liquibase liquibase = new Liquibase(
          properties.getChangeLog(),
          new SpringResourceAccessor(resourceLoader),
          openDatabase
      )
    ) {
      liquibase.update(new Contexts());
    }
  }
}
