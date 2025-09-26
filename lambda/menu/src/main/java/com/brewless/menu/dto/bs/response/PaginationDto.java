package com.brewless.menu.dto.bs.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationDto {
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
}