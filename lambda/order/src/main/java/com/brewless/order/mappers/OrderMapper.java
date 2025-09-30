package com.brewless.order.mappers;

import com.brewless.order.dtos.requests.OrderRequestDto;
import com.brewless.order.dtos.responses.OrderDto;
import com.brewless.order.dtos.responses.OrderResponseDto;
import com.brewless.order.models.bs.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  Order buildOrder(OrderRequestDto orderRequestDto);

  OrderResponseDto buildOrderResponseDto(Order order);

  OrderDto buildOrderDto(Order order);

}
