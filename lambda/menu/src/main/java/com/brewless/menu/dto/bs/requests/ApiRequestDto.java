package com.brewless.menu.dto.bs.requests;

import java.util.Map;
import lombok.Data;

@Data
public class ApiRequestDto<T> {
    private Map<String, String> headers;
    private Map<String, String> queryStringParameters;
    private T body;
}