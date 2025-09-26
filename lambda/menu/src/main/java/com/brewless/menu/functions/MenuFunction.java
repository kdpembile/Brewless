package com.brewless.menu.functions;

import com.brewless.menu.dto.bs.requests.ApiRequestDto;
import com.brewless.menu.dto.bs.requests.MenuRequestDto;
import com.brewless.menu.dto.bs.response.ApiResponseDto;
import com.brewless.menu.exceptions.InvalidRequestException;
import com.brewless.menu.exceptions.MenuException;
import com.brewless.menu.models.bs.Menu;
import com.brewless.menu.services.MenuService;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class MenuFunction {

  private final MenuService menuService;

  @Bean
  public Function<ApiRequestDto<MenuRequestDto>, Mono<ApiResponseDto<List<Menu>>>> getMenu() {
    return menuRequestApiRequestDto -> {
      int page = 0;
      int size = 0;

      Map<String, String> headers = menuRequestApiRequestDto.getHeaders();
      String correlationId = headers.get("x-correlation-id");
      String channel = headers.get("channel-name");

      if (correlationId == null
          || correlationId.isEmpty() || channel == null || channel.isEmpty()) {
        log.error("Invalid request");

        throw new InvalidRequestException(
            "Invalid request", new RuntimeException("Something went wrong"));
      }

      Map<String, String> queryParams = menuRequestApiRequestDto.getQueryParams();

      if (queryParams != null) {
        if (queryParams.containsKey("page")) {
          try {
            page = Integer.parseInt(queryParams.get("page"));
          } catch (NumberFormatException e) {
            throw new InvalidRequestException(e.getMessage(), e);
          }
        }

        if (queryParams.containsKey("size")) {
          try {
            size = Integer.parseInt(queryParams.get("size"));
          } catch (NumberFormatException e) {
            throw new InvalidRequestException(e.getMessage(), e);
          }
        }
      }
      return menuService.getMenu(page, size);
    };
  }
}
