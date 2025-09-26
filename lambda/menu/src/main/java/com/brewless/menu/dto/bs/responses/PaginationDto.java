package com.brewless.menu.dto.bs.responses;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class PaginationDto {

  private int page;
  private int size;
  private long totalItems;
  private int totalPages;
}