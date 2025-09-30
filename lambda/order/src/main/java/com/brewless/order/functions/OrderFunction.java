package com.brewless.order.functions;

import com.brewless.order.dtos.errors.ErrorDto;
import com.brewless.order.dtos.requests.ApiRequestDto;
import com.brewless.order.dtos.requests.OrderRequestDto;
import com.brewless.order.dtos.responses.ApiResponseDto;
import com.brewless.order.dtos.responses.OrderDto;
import com.brewless.order.dtos.responses.OrderResponseDto;
import com.brewless.order.dtos.responses.PaginationDto;
import com.brewless.order.exceptions.InvalidRequestException;
import com.brewless.order.exceptions.OrderException;
import com.brewless.order.services.OrderService;
import com.brewless.order.services.ValidationService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Component
public class OrderFunction {

  private final String applicationName;

  private final OrderService orderService;
  private final ValidationService validationService;

  public OrderFunction(
      @org.springframework.beans.factory.annotation.Value("${spring.application.name}") String applicationName,
      OrderService orderService,
      ValidationService validationService
      ) {
    this.applicationName = applicationName;
    this.orderService = orderService;
    this.validationService = validationService;
  }

  @Bean
  public Function<ApiRequestDto<OrderRequestDto>, Mono<ApiResponseDto<OrderResponseDto>>> createOrder() {
    return request -> validationService.validateHeaders(request)
        .doOnNext(unused -> log.info("{} ::: Creating order...", applicationName))
        .then(orderService.createOrder(request))
        .doOnNext(response ->
            log.info("{} ::: Order ::: {}, successfully created with status of ::: {}"
                , applicationName
                , response.getData().getTxnRefNumber()
                , response.getData().getStatus()))
        .onErrorResume(throwable -> mapError(throwable, "createOrder"));
  }

  @Bean
  public Function<ApiRequestDto<Object>, Mono<ApiResponseDto<List<OrderDto>>>> getOrders() {
    return request -> validationService.validateHeaders(request)
        .then(validationService.validateQueryParams(request))
        .flatMap(tuple2 -> orderService.getOrders(tuple2.getT1(), tuple2.getT2())
            .switchIfEmpty(Mono.defer(() -> buildGetOrdersFallBackResponse(tuple2))))
        .onErrorResume(throwable -> mapError(throwable, "getOrders"));
  }

  @Bean
  public Function<ApiRequestDto<Map<String, String>>, Mono<ApiResponseDto<OrderDto>>> getOrder() {
    return request -> validationService.validateHeaders(request)
        .flatMap(tuple2 -> orderService.getOrder(request.getBody().get("txnRefNumber"))
            .switchIfEmpty(Mono.defer(()-> buildGetOrderFallBackResponse(request))))
        .onErrorResume(throwable -> mapError(throwable, "getOrder"));
  }

  private Mono<ApiResponseDto<OrderDto>> buildGetOrderFallBackResponse(ApiRequestDto<Map<String, String>> request) {

    ApiResponseDto<OrderDto> response = new ApiResponseDto<>();
    response.setMessage(
        "No orders found for txnRefNumber ::: %s"
            .formatted(request.getBody().get("txnRefNumber")));
    response.setTimeStamp(LocalDateTime.now());

    return Mono.just(response);
  }

  private Mono<ApiResponseDto<List<OrderDto>>> buildGetOrdersFallBackResponse(Tuple2<Integer, Integer> tuple2) {
    PaginationDto pagination = PaginationDto.builder()
        .page(tuple2.getT1())
        .size(tuple2.getT2())
        .build();

    String message =
        "No orders found for page %s with size %s"
            .formatted(tuple2.getT1(), tuple2.getT2());

    ApiResponseDto<List<OrderDto>> response = new ApiResponseDto<>();
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

    if (throwable instanceof OrderException orderException) {
      errorDto = ErrorDto.builder()
          .code(orderException.getErrorCode())
          .message(orderException.getMessage())
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
          .code("OS00003")
          .message(throwable.getMessage())
          .build();

      response.setError(errorDto);
    }

    return Mono.just(response);
  }
}
