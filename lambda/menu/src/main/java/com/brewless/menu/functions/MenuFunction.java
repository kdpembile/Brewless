package com.brewless.menu.functions;

import com.brewless.menu.dtos.bs.requests.MenuRequestDto;
import com.brewless.menu.dtos.bs.errors.ErrorDto;
import com.brewless.menu.dtos.bs.requests.ApiRequestDto;
import com.brewless.menu.dtos.bs.responses.ApiResponseDto;
import com.brewless.menu.dtos.bs.responses.PaginationDto;
import com.brewless.menu.exceptions.InvalidRequestException;
import com.brewless.menu.exceptions.MenuException;
import com.brewless.menu.models.bs.Menu;
import com.brewless.menu.services.MenuService;
import com.brewless.menu.services.ValidationService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Component
public class MenuFunction {

  private final String applicationName;

  private final MenuService menuService;

  private final ValidationService validationService;

  public MenuFunction(
      @org.springframework.beans.factory.annotation.Value("${spring.application.name}") String applicationName,
      MenuService menuService, ValidationService validationService
  ) {
    this.applicationName = applicationName;
    this.menuService = menuService;
    this.validationService = validationService;
  }

  @Bean
  public Function<ApiRequestDto<MenuRequestDto>, Mono<ApiResponseDto<List<Menu>>>> getMenu() {
    return request -> validationService.validateHeaders(request)
        .then(validationService.validateQueryParams(request))
        .flatMap(tuple2 -> menuService.getMenu(tuple2.getT1(), tuple2.getT2())
            .switchIfEmpty(Mono.defer(() -> buildFallBackResponse(tuple2))))
        .onErrorResume(throwable -> mapError(throwable, "getMenu"));
  }

  private Mono<ApiResponseDto<List<Menu>>> buildFallBackResponse(Tuple2<Integer, Integer> tuple2) {
    PaginationDto pagination = PaginationDto.builder()
        .page(tuple2.getT1())
        .size(tuple2.getT2())
        .build();

    String message =
        "No menu items found for page %s with size %s"
        .formatted(tuple2.getT1(), tuple2.getT2());

    ApiResponseDto<List<Menu>> response = new ApiResponseDto<>();
    response.setData(Collections.emptyList());
    response.setMessage(message);
    response.setTimeStamp(LocalDateTime.now());
    response.setPagination(pagination);

    return Mono.just(response);
  }

  private <T> Mono<ApiResponseDto<T>> mapError(Throwable throwable, String logContext) {
    log.error("[{}] {} failed", applicationName, logContext, throwable);

    ErrorDto errorDto;
    ApiResponseDto<T> response = new ApiResponseDto<>();

    if (throwable instanceof MenuException menuException) {
      errorDto = ErrorDto.builder()
          .code(menuException.getErrorCode())
          .message(menuException.getMessage())
          .build();

      response.setError(errorDto);
    } else if (throwable instanceof InvalidRequestException invalidRequestException) {
      errorDto = ErrorDto.builder()
          .code(invalidRequestException.getErrorCode())
          .message(invalidRequestException.getMessage())
          .build();

      response.setError(errorDto);
    } else {
      errorDto = ErrorDto.builder()
          .code("MS00003")
          .message(throwable.getMessage())
          .build();

      response.setError(errorDto);
    }

    return Mono.just(response);
  }
}
