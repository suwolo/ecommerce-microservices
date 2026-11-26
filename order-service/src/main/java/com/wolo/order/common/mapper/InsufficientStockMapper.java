package com.wolo.order.common.mapper;

import com.wolo.order.common.exception.InsufficientStockException;
import com.wolo.order.common.exception.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InsufficientStockMapper implements ExceptionMapper<InsufficientStockException> {
    @Override
    public Response toResponse(InsufficientStockException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
    }
}