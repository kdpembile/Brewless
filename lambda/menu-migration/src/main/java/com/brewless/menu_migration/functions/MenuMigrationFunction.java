package com.brewless.menu_migration.functions;

import com.brewless.menu_migration.services.LiquibaseService;
import java.util.function.Supplier;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MenuMigrationFunction {

  private final LiquibaseService liquibaseService;

  @Bean
  public Supplier<String> syncMenu() {
    return () -> {

      try {
        liquibaseService.runMigration();
      } catch (LiquibaseException e) {
        throw new RuntimeException(e);
      }

      return "Successfully sync menu database";
    };
  }
}
