package com.wolo.inventory.api;

import com.wolo.inventory.application.InventoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/api/inventory")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {

    private final InventoryService inventoryService;

    @ConfigProperty(
            name = "inventory.test.response-delay-ms",
            defaultValue = "0"
    )
    long responseDelayMs;

    @Inject
    public InventoryResource(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GET
    @Path("/{id}")
    public Response fetch(@PathParam("id") Long id){
        simulateLatency();
        return Response.ok(inventoryService.retrieveProduct(id)).build();
    }

    private void simulateLatency() {
        if (responseDelayMs <= 0) {
            return;
        }

        try {
            Thread.sleep(responseDelayMs);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}