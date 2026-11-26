package com.wolo.order.infrastructure.client;

import com.wolo.order.common.exception.ProductNotFoundException;
import com.wolo.order.infrastructure.client.dto.InventoryLookupResult;
import com.wolo.order.infrastructure.client.dto.InventoryResponse;
import io.smallrye.faulttolerance.api.ExponentialBackoff;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class InventoryGateway {
    private final InventoryClient inventoryClient;

    private static final Logger LOG = Logger.getLogger(InventoryGateway.class);

    @Inject
    public InventoryGateway(@RestClient InventoryClient inventoryClient){
        this.inventoryClient = inventoryClient;
    }

    @Timeout(1000)
    @Retry(
            maxRetries = 1,
            delay = 500,
            retryOn = {
                    ProcessingException.class,
                    TimeoutException.class
            }
    )
    @CircuitBreaker(
            requestVolumeThreshold = 20,
            failureRatio = 0.3,
            delay = 10000,
            successThreshold = 2,
            failOn = {
                    ProcessingException.class,
                    TimeoutException.class
            },
            skipOn = WebApplicationException.class
    )
    @Fallback(
            fallbackMethod = "inventoryUnavailable",
            skipOn = {WebApplicationException.class, ProductNotFoundException.class}
    )
    public InventoryLookupResult getInventory(Long productId) {
        try {
            InventoryResponse response = inventoryClient.getInventoryByProductId(productId);

            return new InventoryLookupResult(response, true);
        } catch (WebApplicationException exception) {
            if (exception.getResponse().getStatus() == 404) {
                throw new ProductNotFoundException(productId);
            }

            throw exception;
        }
    }

    public InventoryLookupResult inventoryUnavailable(Long productId, Throwable exception){
        LOG.errorf(
                exception,
                "Inventory unavailable for product ID: %d",
                productId
        );

        return new InventoryLookupResult(null, false);
    }
}