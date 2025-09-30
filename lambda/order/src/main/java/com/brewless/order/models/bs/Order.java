package com.brewless.order.models.bs;

import com.brewless.order.dtos.requests.ItemDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("Order")
public class Order {

  @Id
  private String id;

  private String customerName;
  private List<ItemDto> items;
  private String status;
  private String txnRefNumber;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
  private String correlationId;

}
