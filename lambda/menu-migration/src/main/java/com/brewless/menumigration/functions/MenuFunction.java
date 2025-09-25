package com.brewless.menumigration.functions;

import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MenuFunction {

  @Bean
  public Supplier<String> syncMenu() {
    return () -> "Successfully sync menu database";
  }
}
