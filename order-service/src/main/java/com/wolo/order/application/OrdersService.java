package com.wolo.order.application;

import com.wolo.order.api.dto.OrdersRequest;
import com.wolo.order.api.dto.OrdersResponse;
import com.wolo.order.common.exception.InsufficientStockException;
import com.wolo.order.common.exception.InventoryUnavailableException;
import com.wolo.order.common.exception.ProductNotFoundException;
import com.wolo.order.infrastructure.client.InventoryGateway;
import com.wolo.order.infrastructure.client.dto.InventoryLookupResult;
import com.wolo.order.infrastructure.client.dto.InventoryResponse;
import com.wolo.order.infrastructure.persistence.entity.Orders;
import com.wolo.order.infrastructure.persistence.repository.OrdersRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final InventoryGateway inventoryGateway;

    @Inject
    public OrdersService(OrdersRepository ordersRepository,
                         InventoryGateway inventoryGateway){
        this.ordersRepository = ordersRepository;
        this.inventoryGateway = inventoryGateway;
    }

    public OrdersResponse createOrder(OrdersRequest ordersRequest){
        InventoryLookupResult checkStock = inventoryGateway.getInventory(ordersRequest.productId());

        if(!checkStock.serviceAvailable()){
            throw new InventoryUnavailableException();
        }

        InventoryResponse inventory = checkStock.inventoryResponse();

        if(null == inventory){
            throw new ProductNotFoundException(ordersRequest.productId());
        }

        if(inventory.stock() < ordersRequest.quantity()){
            throw new InsufficientStockException(ordersRequest.productId(), ordersRequest.quantity(), inventory.stock());
        }

        Orders order = new Orders(
                ordersRequest.customerId(),
                ordersRequest.productId(),
                ordersRequest.quantity()
        );

        Orders savedOrder = ordersRepository.save(order);

        return toResponse(savedOrder);
    }

    private OrdersResponse toResponse(Orders order) {
        return new OrdersResponse(
                order.getCustomerId(),
                order.getProductId(),
                order.getQuantity(),
                order.getStatus().name()
        );
    }
}