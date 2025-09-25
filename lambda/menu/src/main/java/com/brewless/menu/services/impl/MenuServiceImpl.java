package com.brewless.menu.services.impl;

import com.brewless.menu.services.MenuService;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl implements MenuService {


  @Override
  public List<Map<String, Object>> getMenu() {
    return List.of(
        Map.of("id", "espresso", "name", "Espresso", "price", 120, "size", List.of("Single", "Double")),
        Map.of("id", "latte", "name", "Caffè Latte", "price", 150, "size", List.of("Tall", "Grande", "Venti"))
    );
  }
}
