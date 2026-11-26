package com.wolo.order;

import com.wolo.order.api.dto.OrdersRequest;
import com.wolo.order.api.dto.OrdersResponse;
import com.wolo.order.application.OrdersService;
import com.wolo.order.common.exception.InsufficientStockException;
import com.wolo.order.common.exception.InventoryUnavailableException;
import com.wolo.order.common.exception.ProductNotFoundException;
import com.wolo.order.infrastructure.client.InventoryGateway;
import com.wolo.order.infrastructure.client.dto.InventoryLookupResult;
import com.wolo.order.infrastructure.client.dto.InventoryResponse;
import com.wolo.order.infrastructure.persistence.entity.Orders;
import com.wolo.order.infrastructure.persistence.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdersServiceTest {

    @Mock
    private InventoryGateway inventoryGateway;

    @Mock
    private OrdersRepository ordersRepository;

    private OrdersService ordersService;

    @BeforeEach
    void setUp(){
        ordersService = new OrdersService(
                ordersRepository,
                inventoryGateway
        );
    }

    @Test
    void shouldCreateOrderSuccessfully(){
        OrdersRequest request = new OrdersRequest(
                1L,
                1L,
                3L
        );

        InventoryResponse inventory = new InventoryResponse(
                1L,
                "Americano",
                20L,
                true
        );

        when(inventoryGateway.getInventory(1L))
                .thenReturn(
                        new InventoryLookupResult(inventory, true)
                );

        when(ordersRepository.save(any(Orders.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdersResponse response = ordersService.createOrder(request);

        assertNotNull(response);
        assertEquals(1L, response.customerId());
        assertEquals(1L, response.productId());
        assertEquals(3L, response.quantity());
        assertEquals("CREATED", response.status());

        verify(inventoryGateway, times(1)).getInventory(1L);
        verify(ordersRepository, times(1)).save(any(Orders.class));
    }

    @Test
    void shouldThrowInventoryUnavailableException() {

        OrdersRequest request = new OrdersRequest(
                1L, 1L, 3L
        );

        when(inventoryGateway.getInventory(1L))
                .thenReturn(new InventoryLookupResult(null, false));

        assertThrows(
                InventoryUnavailableException.class,
                () -> ordersService.createOrder(request)
        );

        verify(inventoryGateway).getInventory(1L);
        verifyNoInteractions(ordersRepository);
    }

    @Test
    void shouldThrowProductNotFoundException() {

        OrdersRequest request = new OrdersRequest(
                1L, 1L, 3L
        );

        when(inventoryGateway.getInventory(1L))
                .thenReturn(new InventoryLookupResult(null, true));

        assertThrows(
                ProductNotFoundException.class,
                () -> ordersService.createOrder(request)
        );

        verify(inventoryGateway).getInventory(1L);
        verifyNoInteractions(ordersRepository);
    }

    @Test
    void shouldThrowInsufficientStockException() {

        OrdersRequest request = new OrdersRequest(
                1L, 1L, 25L
        );

        InventoryResponse inventory = new InventoryResponse(
                1L, "Americano", 20L, true
        );

        when(inventoryGateway.getInventory(1L))
                .thenReturn(new InventoryLookupResult(inventory, true));

        assertThrows(
                InsufficientStockException.class,
                () -> ordersService.createOrder(request)
        );

        verify(inventoryGateway).getInventory(1L);
        verifyNoInteractions(ordersRepository);
    }

    @Test
    void shouldCreateOrderWhenStockEqualsRequestedQuantity() {

        OrdersRequest request = new OrdersRequest(
                1L, 1L, 20L
        );

        InventoryResponse inventory = new InventoryResponse(
                1L, "Americano", 20L, true
        );

        when(inventoryGateway.getInventory(1L))
                .thenReturn(new InventoryLookupResult(inventory, true));

        when(ordersRepository.save(any(Orders.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdersResponse response = ordersService.createOrder(request);

        assertNotNull(response);
        assertEquals(20L, response.quantity());

        verify(inventoryGateway).getInventory(1L);
        verify(ordersRepository).save(any(Orders.class));
    }
}