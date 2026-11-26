package com.wolo.order.common.mapper;

import com.wolo.order.common.exception.ProductNotFoundException;
import com.wolo.order.common.exception.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ProductNotFoundMapper implements ExceptionMapper<ProductNotFoundException> {
    @Override
    public Response toResponse(ProductNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
    }
}