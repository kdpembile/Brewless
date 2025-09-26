package com.brewless.menu_migration.wrapper;

import com.brewless.menu_migration.exceptions.MigrationException;
import com.brewless.menu_migration.models.ErrorDto;
import com.brewless.menu_migration.models.MigrationResponseDto;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.function.context.catalog.FunctionAroundWrapper;
import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry.FunctionInvocationWrapper;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MenuMigrationFunctionWrapper extends FunctionAroundWrapper {

  private final String applicationName;

  public MenuMigrationFunctionWrapper(
      @org.springframework.beans.factory.annotation.Value("${spring.application.name}") String applicationName
  ) {
    this.applicationName = applicationName;
  }

  @Override
  protected Object doApply(Object input, FunctionInvocationWrapper targetFunction) {
    try {
      return targetFunction.apply(input);
    } catch (MigrationException e) {

      log.error("[{}] Migration failed", applicationName, e);

      return MessageBuilder.withPayload(
          MigrationResponseDto.builder()
              .message("Migration service failed")
              .errorCode(ErrorDto.builder()
                  .code(e.getErrorCode())
                  .message(e.getMessage())
                  .build())
              .localDateTime(LocalDateTime.now())
              .build())
          .build();

    } catch (Exception e) {
      log.error("[{}] Unexpected error", applicationName, e);

      return MessageBuilder.withPayload(
          MigrationResponseDto.builder()
              .message("Migration service failed")
              .errorCode(ErrorDto.builder()
                  .code("MM00002")
                  .message(e.getMessage())
                  .build())
              .localDateTime(LocalDateTime.now())
              .build())
          .build();
    }
  }
}