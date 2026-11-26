package com.wolo.inventory.infrastructure.persistence;

import com.wolo.inventory.infrastructure.persistence.entity.Products;
import com.wolo.inventory.infrastructure.persistence.repository.InventoryRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProductsDataSeeder {

    private final InventoryRepository inventoryRepository;

    public ProductsDataSeeder(InventoryRepository inventoryRepository){
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    void seed(@Observes StartupEvent event){
        List<Products> productsList = dataProducts();
        inventoryRepository.persist(productsList);
    }

    private List<Products> dataProducts(){
        List<Products> products = new ArrayList<>();
        products.add(new Products("Americano", 10L, true));
        products.add(new Products("Espresso", 100L, true));
        products.add(new Products("Latte", 80L, true));
        products.add(new Products("Piccolo", 7L, true));
        return products;
    }

}