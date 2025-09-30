package com.brewless.order.dtos.responses;

import com.brewless.order.dtos.errors.ErrorDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {

  private T data;
  private ErrorDto error;
  private String message;
  private LocalDateTime timeStamp;
  private PaginationDto pagination;
}
