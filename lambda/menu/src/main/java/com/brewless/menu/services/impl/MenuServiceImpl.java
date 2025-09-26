package com.brewless.menu.services.impl;

import com.brewless.menu.dto.bs.responses.ApiResponseDto;
import com.brewless.menu.dto.bs.responses.PaginationDto;
import com.brewless.menu.models.bs.Menu;
import com.brewless.menu.repositories.bs.MenuRepository;
import com.brewless.menu.services.MenuService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class MenuServiceImpl implements MenuService {

  private final MenuRepository menuRepository;

  @Override
  public Mono<ApiResponseDto<List<Menu>>> getMenu(int page, int size) {

    return menuRepository.count()
        .flatMap(aLong -> {
          if (aLong == 0) {
            log.info("No menu items found for page {} with size {}", page, size);
            return Mono.empty();
          }
          return Mono.just(aLong);
        })
        .zipWith(menuRepository.findAll()
            .skip((long) page * size)
            .take(size)
            .collectList())
        .flatMap(tuple2 -> {
          PaginationDto pagination = PaginationDto.builder()
              .page(page)
              .size(size)
              .totalItems(tuple2.getT1())
              .totalPages((int) Math.ceil((double) tuple2.getT1() / size))
              .build();

          ApiResponseDto<List<Menu>> response = new ApiResponseDto<>();
          response.setData(tuple2.getT2());
          response.setMessage("Menus fetched successfully");
          response.setTimeStamp(LocalDateTime.now());
          response.setPaginationDto(pagination);

          return Mono.just(response);
        });
  }
}
