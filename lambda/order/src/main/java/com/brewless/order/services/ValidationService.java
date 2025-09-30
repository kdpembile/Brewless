package com.brewless.order.services;

import com.brewless.order.dtos.requests.ApiRequestDto;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface ValidationService {

  Mono<Void> validateHeaders(
      ApiRequestDto<?> requestDto);

  Mono<Tuple2<Integer, Integer>> validateQueryParams(
      ApiRequestDto<?> requestDto);

}
