package com.brewless.order.dtos.requests;

import lombok.Data;

@Data
public class ItemDto {

  private String name;
  private String size;
  private Integer quantity;

}
