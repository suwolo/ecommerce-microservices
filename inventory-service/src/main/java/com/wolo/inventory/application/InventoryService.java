package com.wolo.inventory.application;

import com.wolo.inventory.api.dto.ProductResponse;
import com.wolo.inventory.common.exception.ProductNotFoundException;
import com.wolo.inventory.infrastructure.persistence.entity.Products;
import com.wolo.inventory.infrastructure.persistence.repository.InventoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.Optional;

@ApplicationScoped
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Inject
    public InventoryService(InventoryRepository inventoryRepository){
        this.inventoryRepository = inventoryRepository;
    }

    public ProductResponse retrieveProduct(Long id){
        Products product = inventoryRepository.findByIdOptional(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getStock(),
                product.isAvailable()
        );
    }
}