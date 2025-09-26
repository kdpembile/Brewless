package com.brewless.menu.services;

import com.brewless.menu.dto.bs.responses.ApiResponseDto;
import com.brewless.menu.models.bs.Menu;
import java.util.List;
import reactor.core.publisher.Mono;

public interface MenuService {

  Mono<ApiResponseDto<List<Menu>>> getMenu(int page, int size);

}
