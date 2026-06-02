package com.example.demo.payment.infrastructure;

import com.example.demo.payment.domain.model.PaymentFailure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentFailureJpaRepository extends JpaRepository<PaymentFailure, UUID> {
}
