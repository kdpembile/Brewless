package com.brewless.order.dtos.requests;


import java.util.List;
import lombok.Data;

@Data
public class OrderRequestDto {

  private String customerName;
  private List<ItemDto> items;

}
