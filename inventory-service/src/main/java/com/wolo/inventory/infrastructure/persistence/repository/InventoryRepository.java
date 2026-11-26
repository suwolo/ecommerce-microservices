package com.wolo.inventory.infrastructure.persistence.repository;

import com.wolo.inventory.infrastructure.persistence.entity.Products;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryRepository implements PanacheRepository<Products> {
}