package com.brewless.menu_migration.functions;

import com.brewless.menu_migration.models.MigrationResponseDto;
import com.brewless.menu_migration.services.LiquibaseService;
import java.time.LocalDateTime;
import java.util.function.Supplier;
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
  public Supplier<MigrationResponseDto> syncMenu() {
    return () -> {
      liquibaseService.runMigration();
      return MigrationResponseDto.builder()
          .message("Successfully sync Menu")
          .localDateTime(LocalDateTime.now())
          .build();
    };
  }
}
