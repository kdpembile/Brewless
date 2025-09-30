package com.brewless.order.dtos.responses;

import com.brewless.order.dtos.requests.ItemDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {

  private String customerName;
  private List<ItemDto> items;
  private String status;
  private String txnRefNumber;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

}
