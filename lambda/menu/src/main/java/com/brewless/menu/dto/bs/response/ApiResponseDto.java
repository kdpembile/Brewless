package com.brewless.menu.dto.bs.response;

import com.brewless.menu.dto.bs.errors.ErrorDto;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ApiResponseDto<T> {
  private T data;
  private ErrorDto errorDto;
  private String message;
  private LocalDateTime timeStamp;
  private PaginationDto paginationDto;
}
