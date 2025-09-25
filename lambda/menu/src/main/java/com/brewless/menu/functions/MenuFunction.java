package com.brewless.menu.functions;

import com.brewless.menu.services.MenuService;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MenuFunction {

  private final MenuService menuService;

  @Bean
  public Supplier<List<Map<String, Object>>> getMenu() {
    return menuService::getMenu;
  }
}
