package com.brewless.menu.functions;

import com.brewless.menu.dto.bs.requests.MenuRequestDto;
import com.brewless.menu.dto.bs.errors.ErrorDto;
import com.brewless.menu.dto.bs.requests.ApiRequestDto;
import com.brewless.menu.dto.bs.responses.ApiResponseDto;
import com.brewless.menu.dto.bs.responses.PaginationDto;
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
        .onErrorResume(this::mapError);
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
    response.setPaginationDto(pagination);

    return Mono.just(response);
  }

  private Mono<ApiResponseDto<List<Menu>>> mapError(Throwable throwable) {
    if (throwable instanceof MenuException menuException) {
      log.error("[{}] Menu fetch failed", applicationName, menuException);

      ApiResponseDto<List<Menu>> apiResponseDto = new ApiResponseDto<>();
      apiResponseDto.setErrorDto(ErrorDto.builder()
          .code(menuException.getErrorCode())
          .message(menuException.getMessage())
          .build());

      return Mono.just(apiResponseDto);
    } else if (throwable instanceof InvalidRequestException invalidRequestException) {
      log.error("[{}] Menu fetch failed", applicationName, invalidRequestException);

      ApiResponseDto<List<Menu>> apiResponseDto = new ApiResponseDto<>();
      apiResponseDto.setErrorDto(ErrorDto.builder()
          .code(invalidRequestException.getErrorCode())
          .message(invalidRequestException.getMessage())
          .build());

    }

    ApiResponseDto<List<Menu>> apiResponseDto = new ApiResponseDto<>();
    apiResponseDto.setErrorDto(ErrorDto.builder()
        .code("M00002")
        .message(throwable.getMessage())
        .build());

    return Mono.just(apiResponseDto);
  }
}
