package com.example.demo.order.domain.repository;

import com.example.demo.order.domain.model.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    List<Order> findAll();

    Optional<Order> findById(UUID id);

    Optional<Order> findByOrderNo(String orderNo);

    Order save(Order order);

    List<Order> findUnsettledPaidOrders(LocalDateTime fromInclusive, LocalDateTime toExclusive);
}
