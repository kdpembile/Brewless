package com.brewless.order.enums;

import lombok.Getter;

@Getter
public enum Status {

  PENDING("PENDING");

  private final String value;

  Status(String value) {
    this.value = value;
  }
}
