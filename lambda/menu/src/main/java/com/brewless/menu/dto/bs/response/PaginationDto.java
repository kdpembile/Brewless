package com.brewless.menu.dto.bs.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PaginationDto {

  private int page;
  private int size;
  private long totalItems;
  private int totalPages;
}