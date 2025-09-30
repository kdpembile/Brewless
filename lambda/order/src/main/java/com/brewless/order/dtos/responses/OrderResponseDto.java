package com.brewless.order.dtos.responses;

import lombok.Data;

@Data
public class OrderResponseDto {

  private String txnRefNumber;
  private String status;

}
