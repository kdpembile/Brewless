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
        .then(orderService.createOrder(request.getBody()))
        .doOnNext(response ->
            log.info("{} ::: Order ::: {}, successfully created with status of ::: {}"
                , applicationName
                , response.getData().getTxnRefNumber()
                , response.getData().getStatus()));
  }

  @Bean
  public Function<ApiRequestDto<?>, Mono<ApiResponseDto<List<OrderDto>>>> getOrders() {
    return request -> validationService.validateHeaders(request)
        .then(validationService.validateQueryParams(request))
        .flatMap(tuple2 -> orderService.getOrders(tuple2.getT1(), tuple2.getT2())
            .switchIfEmpty(Mono.defer(() -> buildGetOrdersFallBackResponse(tuple2))))
        .onErrorResume(this::mapGetOrdersError);
  }

  @Bean
  public Function<ApiRequestDto<Map<String, String>>, Mono<ApiResponseDto<OrderDto>>> getOrder() {
    return request -> validationService.validateHeaders(request)
        .flatMap(tuple2 -> orderService.getOrder(request.getBody().get("txnRefNumber"))
            .switchIfEmpty(Mono.defer(()-> buildGetOrderFallBackResponse(request))))
        .onErrorResume(this::mapGetOrderError);
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
    response.setPaginationDto(pagination);

    return Mono.just(response);
  }

  private Mono<ApiResponseDto<List<OrderDto>>> mapGetOrdersError(Throwable throwable) {
    if (throwable instanceof OrderException orderException) {
      log.error("[{}] Orders fetch failed", applicationName, orderException);

      ApiResponseDto<List<OrderDto>> apiResponseDto = new ApiResponseDto<>();
      apiResponseDto.setErrorDto(ErrorDto.builder()
          .code(orderException.getErrorCode())
          .message(orderException.getMessage())
          .build());

      return Mono.just(apiResponseDto);

    } else if (throwable instanceof InvalidRequestException invalidRequestException) {
      log.error("[{}] Orders fetch failed", applicationName, invalidRequestException);

      ApiResponseDto<List<OrderDto>> apiResponseDto = new ApiResponseDto<>();
      apiResponseDto.setErrorDto(ErrorDto.builder()
          .code(invalidRequestException.getErrorCode())
          .message(invalidRequestException.getMessage())
          .build());

    }

    ApiResponseDto<List<OrderDto>> apiResponseDto = new ApiResponseDto<>();
    apiResponseDto.setErrorDto(ErrorDto.builder()
        .code("OS00003")
        .message(throwable.getMessage())
        .build());

    return Mono.just(apiResponseDto);
  }

  private Mono<ApiResponseDto<OrderDto>> mapGetOrderError(Throwable throwable) {
    if (throwable instanceof OrderException orderException) {
      log.error("[{}] Order fetch failed", applicationName, orderException);

      ApiResponseDto<OrderDto> response = new ApiResponseDto<>();
      response.setErrorDto(ErrorDto.builder()
          .code(orderException.getErrorCode())
          .message(orderException.getMessage())
          .build());

      return Mono.just(response);

    } else if (throwable instanceof InvalidRequestException invalidRequestException) {
      log.error("[{}] Order fetch failed", applicationName, invalidRequestException);

      ApiResponseDto<OrderDto> response = new ApiResponseDto<>();
      response.setErrorDto(ErrorDto.builder()
          .code(invalidRequestException.getErrorCode())
          .message(invalidRequestException.getMessage())
          .build());

    }

    ApiResponseDto<OrderDto> response = new ApiResponseDto<>();
    response.setErrorDto(ErrorDto.builder()
        .code("OS00003")
        .message(throwable.getMessage())
        .build());

    return Mono.just(response);
  }
}
