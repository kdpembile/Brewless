package com.brewless.order.dtos.requests;

import java.util.Map;
import lombok.Data;

@Data
public class ApiRequestDto<T> {

    private Map<String, String> headers;
    private Map<String, String> queryStringParameters;
    private T body;

}