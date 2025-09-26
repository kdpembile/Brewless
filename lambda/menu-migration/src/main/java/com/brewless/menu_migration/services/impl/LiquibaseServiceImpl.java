package com.brewless.menu_migration.services.impl;

import com.brewless.menu_migration.exceptions.MigrationException;
import com.brewless.menu_migration.services.LiquibaseService;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LiquibaseServiceImpl implements LiquibaseService {

  private final LiquibaseProperties properties;

  private final ResourceLoader resourceLoader;

  @Override
  public void runMigration() {
    try {
      // Open database in try-with-resources
      try (Database database = DatabaseFactory.getInstance()
          .openDatabase(properties.getUrl(),
              null,
              null,
              null,
              null);

          Liquibase liquibase = new Liquibase(
              properties.getChangeLog(),
              new SpringResourceAccessor(resourceLoader),
              database)) {

        liquibase.update(new Contexts());

      }
    } catch (LiquibaseException e) {
      throw new MigrationException("Liquibase migration failed", e);
    }
  }
}
