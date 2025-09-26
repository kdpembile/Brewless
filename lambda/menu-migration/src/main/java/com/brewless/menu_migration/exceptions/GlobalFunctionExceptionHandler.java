package com.brewless.menu_migration.exceptions;

import com.brewless.menu_migration.models.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.function.context.catalog.FunctionAroundWrapper;
import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry.FunctionInvocationWrapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GlobalFunctionExceptionHandler extends FunctionAroundWrapper {

  private final String applicationName;

  public GlobalFunctionExceptionHandler(
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
      return new ErrorResponseDto(e.getErrorCode(), "Liquibase migration failed");

    } catch (Exception e) {
      log.error("[{}] Unexpected error", applicationName, e);
      return new ErrorResponseDto("MM_0002", "Unexpected server error");
    }
  }
}