package com.wolo.inventory.common.mapper;

import com.wolo.inventory.common.exception.ProductNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class ProductNotFoundMapper implements ExceptionMapper<ProductNotFoundException> {

    @Override
    public Response toResponse(ProductNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("message", e.getMessage()))
                .build();
    }
}