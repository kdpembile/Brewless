package com.brewless.order.services.impl;

import com.brewless.order.dtos.requests.ApiRequestDto;
import com.brewless.order.dtos.requests.OrderRequestDto;
import com.brewless.order.dtos.responses.ApiResponseDto;
import com.brewless.order.dtos.responses.OrderDto;
import com.brewless.order.dtos.responses.OrderResponseDto;
import com.brewless.order.dtos.responses.PaginationDto;
import com.brewless.order.enums.Status;
import com.brewless.order.mappers.OrderMapper;
import com.brewless.order.repositories.bs.OrderRepository;
import com.brewless.order.services.OrderService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  @Override
  public Mono<ApiResponseDto<OrderResponseDto>> createOrder(ApiRequestDto<OrderRequestDto> request) {
    return Mono.just(orderMapper.buildOrder(request.getBody()))
        .flatMap(order -> {
          order.setStatus(Status.PENDING.getValue());
          order.setCorrelationId(request.getHeaders().get("x-correlation-id"));
          order.setCreatedDate(LocalDateTime.now());
          order.setUpdatedDate(LocalDateTime.now());

          return orderRepository.save(order);
        })
        .doOnNext(order -> log.info("Order saved: {}", order))
        .flatMap(order -> {
          OrderResponseDto orderResponseDto = orderMapper.buildOrderResponseDto(order);

          ApiResponseDto<OrderResponseDto> apiResponseDto = new ApiResponseDto<>();
          apiResponseDto.setData(orderResponseDto);
          apiResponseDto.setMessage("Order received!");
          apiResponseDto.setTimeStamp(LocalDateTime.now());

          return Mono.just(apiResponseDto);
        });
  }

  @Override
  public Mono<ApiResponseDto<List<OrderDto>>> getOrders(int page, int size) {
    return orderRepository.count()
        .flatMap(aLong -> {
          if (aLong == 0) {
            log.info("No orders found for page {} with size {}", page, size);
            return Mono.empty();
          }
          return Mono.just(aLong);
        })
        .zipWith(orderRepository.findAll()
            .skip((long) page * size)
            .take(size)
            .collectList())
        .flatMap(tuple2 -> {
          PaginationDto pagination = PaginationDto.builder()
              .page(page)
              .size(size)
              .totalItems(tuple2.getT1())
              .totalPages((int) Math.ceil((double) tuple2.getT1() / size))
              .build();

          ApiResponseDto<List<OrderDto>> response = new ApiResponseDto<>();
          response.setData(tuple2.getT2().stream()
              .map(orderMapper::buildOrderDto)
              .toList());
          response.setMessage("Orders fetched successfully");
          response.setTimeStamp(LocalDateTime.now());
          response.setPagination(pagination);

          return Mono.just(response);
        });
  }

  @Override
  public Mono<ApiResponseDto<OrderDto>> getOrder(String txnRefNumber) {
    return orderRepository.findByTxnRefNumber(txnRefNumber)
        .map(orderMapper::buildOrderDto)
        .flatMap(orderDto -> {

          ApiResponseDto<OrderDto> response = new ApiResponseDto<>();
          response.setData(orderDto);
          response.setMessage("Order fetched successfully");
          response.setTimeStamp(LocalDateTime.now());

          return Mono.just(response);
        });
  }
}
