package com.brewless.menu.models.bs;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("Menu")
public class Menu {

  @Id
  private String id;

  private String name;

  private BigDecimal price;

  private String sizes;

  private String description;

  private String imageUrl;

}
