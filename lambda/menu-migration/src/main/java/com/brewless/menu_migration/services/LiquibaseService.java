package com.brewless.menu_migration.services;

import liquibase.exception.LiquibaseException;

public interface LiquibaseService {

  void runMigration() throws LiquibaseException;

}
