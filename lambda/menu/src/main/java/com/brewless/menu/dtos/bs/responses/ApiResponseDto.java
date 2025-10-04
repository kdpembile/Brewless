package com.brewless.menu.dtos.bs.responses;

import com.brewless.menu.dtos.bs.errors.ErrorDto;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ApiResponseDto<T> {

  private T data;
  private ErrorDto error;
  private String message;
  private LocalDateTime timeStamp;
  private PaginationDto pagination;
}
