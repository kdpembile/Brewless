package com.brewless.menu.services;

import com.brewless.menu.dto.bs.requests.ApiRequestDto;
import com.brewless.menu.dto.bs.requests.MenuRequestDto;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface ValidationService {

  Mono<Void> validateHeaders(
      ApiRequestDto<MenuRequestDto> menuRequestApiRequestDto);

  Mono<Tuple2<Integer, Integer>> validateQueryParams(
      ApiRequestDto<MenuRequestDto> menuRequestApiRequestDto);

}
