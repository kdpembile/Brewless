package com.brewless.menu_migration.functions;

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

  private final SpringLiquibase liquibase;

  @Bean
  public Supplier<String> syncMenu() {
    return () -> {

      try {
        liquibase.afterPropertiesSet();
      } catch (LiquibaseException e) {
        throw new RuntimeException(e);
      }

      return "Successfully sync menu database";
    };
  }
}
