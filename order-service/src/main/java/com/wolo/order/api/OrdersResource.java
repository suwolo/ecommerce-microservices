package com.wolo.order.api;

import com.wolo.order.api.dto.OrdersRequest;
import com.wolo.order.api.dto.OrdersResponse;
import com.wolo.order.application.OrdersService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/order")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrdersResource {

    private final OrdersService ordersService;

    @Inject
    public OrdersResource(OrdersService ordersService){
        this.ordersService = ordersService;
    }

    @POST
    public Response createOrder(@Valid OrdersRequest request){
        OrdersResponse resp = ordersService.createOrder(request);

        return Response.status(Response.Status.CREATED).entity(resp).build();
    }
}
