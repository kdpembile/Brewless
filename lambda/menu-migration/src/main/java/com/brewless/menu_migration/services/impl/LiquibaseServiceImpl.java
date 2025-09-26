package com.brewless.menu_migration.services.impl;

import com.brewless.menu_migration.services.LiquibaseService;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LiquibaseServiceImpl implements LiquibaseService {

  private final LiquibaseProperties properties;

  private final ResourceLoader resourceLoader;

  @Override
  public void runMigration() throws LiquibaseException {
    Database database = DatabaseFactory.getInstance()
        .openDatabase(
            properties.getUrl(),
            null,
            null,
            null,
            null
        );

    try (Liquibase liquibase = new Liquibase(
        properties.getChangeLog(),
        new SpringResourceAccessor(resourceLoader),
        database
    )) {
      liquibase.update(new Contexts());
    }
  }
}
