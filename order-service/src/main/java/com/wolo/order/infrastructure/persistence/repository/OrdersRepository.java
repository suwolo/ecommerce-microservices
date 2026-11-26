package com.wolo.order.infrastructure.persistence.repository;

import com.wolo.order.infrastructure.persistence.entity.Orders;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrdersRepository implements PanacheRepository<Orders> {

    @Transactional
    public Orders save(Orders order){
        persist(order);

        return order;
    }
}
