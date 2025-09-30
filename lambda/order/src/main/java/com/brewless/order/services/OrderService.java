package com.brewless.order.services;

import com.brewless.order.dtos.requests.ApiRequestDto;
import com.brewless.order.dtos.requests.OrderRequestDto;
import com.brewless.order.dtos.responses.ApiResponseDto;
import com.brewless.order.dtos.responses.OrderDto;
import com.brewless.order.dtos.responses.OrderResponseDto;
import java.util.List;
import reactor.core.publisher.Mono;

public interface OrderService {

  Mono<ApiResponseDto<OrderResponseDto>> createOrder(ApiRequestDto<OrderRequestDto> request);

  Mono<ApiResponseDto<List<OrderDto>>> getOrders(int page, int size);

  Mono<ApiResponseDto<OrderDto>> getOrder(String txnRefNumber);

}
