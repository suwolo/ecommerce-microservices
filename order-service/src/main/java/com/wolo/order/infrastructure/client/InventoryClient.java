package com.wolo.order.infrastructure.client;

import com.wolo.order.infrastructure.client.dto.InventoryResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/inventory")
@RegisterRestClient(configKey = "inventory-api")
@Produces(MediaType.APPLICATION_JSON)
public interface InventoryClient {

    @GET
    @Path("/{productId}")
    InventoryResponse getInventoryByProductId(@PathParam("productId") Long productId);
}
