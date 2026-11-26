package com.wolo.order.common.mapper;

import com.wolo.order.common.exception.InventoryUnavailableException;
import com.wolo.order.common.exception.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InventoryUnavailableMapper implements ExceptionMapper<InventoryUnavailableException> {
    @Override
    public Response toResponse(InventoryUnavailableException e) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
    }
}