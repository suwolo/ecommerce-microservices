package com.wolo.inventory;

import com.wolo.inventory.api.dto.ProductResponse;
import com.wolo.inventory.application.InventoryService;
import com.wolo.inventory.common.exception.ProductNotFoundException;
import com.wolo.inventory.infrastructure.persistence.entity.Products;
import com.wolo.inventory.infrastructure.persistence.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    private InventoryRepository inventoryRepository;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp(){
        inventoryRepository = mock(InventoryRepository.class);

        inventoryService = new InventoryService(
                inventoryRepository
        );
    }

    @Test
    void shouldRetrieveProductSuccessfully(){
        when(inventoryRepository.findByIdOptional(anyLong()))
                .thenReturn(
                        Optional.of(new Products(
                                "Americano",
                                10L,
                                true
                        ))
                );

        ProductResponse response = inventoryService.retrieveProduct(1L);

        assertNotNull(response);
        assertEquals("Americano", response.name());
        assertEquals(10L, response.stock());
        assertTrue(response.available());

        verify(inventoryRepository, times(1)).findByIdOptional(1L);
    }

    @Test
    void shouldThrowProductNotFoundException(){
        when(inventoryRepository.findByIdOptional(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> inventoryService.retrieveProduct(1L)
        );

        verify(inventoryRepository, times(1)).findByIdOptional(1L);
    }
}