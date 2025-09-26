package com.brewless.menu.services.impl;

import com.brewless.menu.dto.bs.requests.ApiRequestDto;
import com.brewless.menu.dto.bs.requests.MenuRequestDto;
import com.brewless.menu.exceptions.InvalidRequestException;
import com.brewless.menu.services.ValidationService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
@Service
public class ValidationServiceImpl implements ValidationService {

  public static final String INVALID_REQUEST = "Invalid request";

  @Override
  public Mono<Void> validateHeaders(ApiRequestDto<MenuRequestDto> menuRequestApiRequestDto) {
    Map<String, String> headers = menuRequestApiRequestDto.getHeaders();
    String correlationId = headers.get("x-correlation-id");
    String channel = headers.get("channel-name");

    if (correlationId == null
        || correlationId.isEmpty() || channel == null || channel.isEmpty()) {
      log.error(INVALID_REQUEST);

      return Mono.error(new InvalidRequestException(
          INVALID_REQUEST, new RuntimeException("Something went wrong")));
    }
    return Mono.empty();
  }

  @Override
  public Mono<Tuple2<Integer, Integer>> validateQueryParams(
      ApiRequestDto<MenuRequestDto> menuRequestApiRequestDto) {
    int page = 0;
    int size = 100;

    Map<String, String> queryParams = menuRequestApiRequestDto.getQueryStringParameters();

    if (queryParams != null) {
      if (queryParams.containsKey("page")) {
        try {
          page = Integer.parseInt(queryParams.get("page"));
        } catch (NumberFormatException e) {
          return Mono.error(new InvalidRequestException(INVALID_REQUEST, e));
        }
      }

      if (queryParams.containsKey("size")) {
        try {
          size = Integer.parseInt(queryParams.get("size"));
        } catch (NumberFormatException e) {
          return Mono.error(new InvalidRequestException(INVALID_REQUEST, e));
        }
      }
    }
    return Mono.just(Tuples.of(page, size));
  }
}
