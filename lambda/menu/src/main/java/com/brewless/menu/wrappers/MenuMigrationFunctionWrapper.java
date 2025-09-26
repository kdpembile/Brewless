package com.brewless.menu.wrappers;

import com.brewless.menu.dto.bs.errors.ErrorDto;
import com.brewless.menu.dto.bs.response.ApiResponseDto;
import com.brewless.menu.exceptions.MenuException;
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
    } catch (MenuException e) {

      log.error("[{}] Migration failed", applicationName, e);

      ApiResponseDto<ErrorDto> apiResponseDto = new ApiResponseDto<>();
      apiResponseDto.setErrorDto(ErrorDto.builder()
              .code(e.getErrorCode())
              .message(e.getMessage())
          .build());

      return MessageBuilder.withPayload(apiResponseDto).build();

    } catch (Exception e) {
      log.error("[{}] Unexpected error", applicationName, e);

      ApiResponseDto<ErrorDto> apiResponseDto = new ApiResponseDto<>();
      apiResponseDto.setErrorDto(ErrorDto.builder()
          .code("M00002")
          .message(e.getMessage())
          .build());

      return MessageBuilder.withPayload(apiResponseDto).build();
    }
  }
}