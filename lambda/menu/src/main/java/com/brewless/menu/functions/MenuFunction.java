package com.brewless.menu.functions;

import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class MenuFunction {

  @Bean
  public Function<Flux<String>, Flux<String>> uppercase() {
    return flux -> flux.map(String::toUpperCase);
  }
}
