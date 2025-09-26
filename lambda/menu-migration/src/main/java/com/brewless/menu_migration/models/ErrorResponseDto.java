package com.brewless.menu_migration.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponseDto {

  private String code;
  private String message;

}
