package com.brewless.menu_migration.functions;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MenuMigrationFunction {

  @Bean
  public Function<String, String> syncMenu() {
    return payload -> {
      log.info("Request payload: {}", payload);
      return "Successfully sync menu database";
    };
  }
}
