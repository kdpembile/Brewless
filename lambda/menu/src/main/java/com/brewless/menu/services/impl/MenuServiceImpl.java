package com.brewless.menu.services.impl;

import com.brewless.menu.dto.bs.response.ApiResponseDto;
import com.brewless.menu.dto.bs.response.PaginationDto;
import com.brewless.menu.models.bs.Menu;
import com.brewless.menu.repositories.bs.MenuRepository;
import com.brewless.menu.services.MenuService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class MenuServiceImpl implements MenuService {

  private final MenuRepository menuRepository;

  @Override
  public Mono<ApiResponseDto<List<Menu>>> getMenu(int page, int size) {

    Mono<Long> totalCount = menuRepository.count();

    Flux<Menu> items = menuRepository.findAll()
        .skip((long) page * size)
        .take(size);

    return totalCount.flatMap(total ->
        items.collectList().map(menuList -> {
          PaginationDto pagination = new PaginationDto(
              page,
              size,
              total,
              (int) Math.ceil((double) total / size)
          );

          ApiResponseDto<List<Menu>> response = new ApiResponseDto<>();
          response.setData(menuList);
          response.setMessage("Menus fetched successfully");
          response.setTimeStamp(LocalDateTime.now());
          response.setPaginationDto(pagination);

          return response;
        }));
  }
}
