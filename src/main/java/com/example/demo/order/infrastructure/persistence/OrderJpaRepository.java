package com.example.demo.order.infrastructure.persistence;

import com.example.demo.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderNo(String orderNo);

    List<Order> findByStatusAndSettledFalseAndPaidAtGreaterThanEqualAndPaidAtLessThan(
            String status,
            LocalDateTime fromInclusive,
            LocalDateTime toExclusive
    );
}
