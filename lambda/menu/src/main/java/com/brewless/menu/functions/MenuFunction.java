package com.brewless.menu.functions;

import com.brewless.menu.dto.bs.requests.ApiRequestDto;
import com.brewless.menu.dto.bs.requests.MenuRequestDto;
import com.brewless.menu.dto.bs.response.ApiResponseDto;
import com.brewless.menu.dto.bs.response.PaginationDto;
import com.brewless.menu.exceptions.InvalidRequestException;
import com.brewless.menu.models.bs.Menu;
import com.brewless.menu.services.MenuService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
@RequiredArgsConstructor
@Component
public class MenuFunction {

  private final MenuService menuService;

  @Bean
  public Function<ApiRequestDto<MenuRequestDto>, Mono<ApiResponseDto<List<Menu>>>> getMenu() {
    return request -> Mono.just(validateHeaders(request))
        .then(validateQueryParams(request))
        .flatMap(tuple2 -> menuService.getMenu(tuple2.getT1(), tuple2.getT2())
            .switchIfEmpty(Mono.defer(() -> {
              PaginationDto pagination = PaginationDto.builder()
                  .page(tuple2.getT1())
                  .size(tuple2.getT2())
                  .build();

              String message = "No menu items found for page %s with size %s"
                  .formatted(tuple2.getT1(), tuple2.getT2());

              ApiResponseDto<List<Menu>> response = new ApiResponseDto<>();
              response.setData(Collections.emptyList());
              response.setMessage(message);
              response.setTimeStamp(LocalDateTime.now());
              response.setPaginationDto(pagination);

              return Mono.just(response);
            })));
  }

  private Mono<Tuple2<Integer, Integer>> validateQueryParams(
      ApiRequestDto<MenuRequestDto> menuRequestApiRequestDto) {
    int page = 0;
    int size = 0;

    Map<String, String> queryParams = menuRequestApiRequestDto.getQueryParams();

    if (queryParams != null) {
      if (queryParams.containsKey("page")) {
        try {
          page = Integer.parseInt(queryParams.get("page"));
        } catch (NumberFormatException e) {
          return Mono.error(new InvalidRequestException(e.getMessage(), e));
        }
      }

      if (queryParams.containsKey("size")) {
        try {
          size = Integer.parseInt(queryParams.get("size"));
        } catch (NumberFormatException e) {
          return Mono.error(new InvalidRequestException(e.getMessage(), e));
        }
      }
    }
    return Mono.just(Tuples.of(page, size));
  }


  private Mono<Void> validateHeaders(
      ApiRequestDto<MenuRequestDto> menuRequestApiRequestDto) {

    Map<String, String> headers = menuRequestApiRequestDto.getHeaders();
    String correlationId = headers.get("x-correlation-id");
    String channel = headers.get("channel-name");

    if (correlationId == null
        || correlationId.isEmpty() || channel == null || channel.isEmpty()) {
      log.error("Invalid request");

      return Mono.error(new InvalidRequestException(
          "Invalid request", new RuntimeException("Something went wrong")));
    }
    return Mono.empty();
  }
}
