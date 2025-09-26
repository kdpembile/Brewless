package com.brewless.menu_migration.models;

import java.time.LocalDateTime;
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
public class MigrationResponseDto {

  private String message;
  private ErrorDto errorCode;
  private LocalDateTime localDateTime;
}
