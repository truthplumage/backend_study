package com.example.demo.payment.infrastructure;

import com.example.demo.payment.domain.model.PaymentFailure;
import com.example.demo.payment.domain.repository.PaymentFailureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentFailureRepositoryAdapter implements PaymentFailureRepository {

    private final PaymentFailureJpaRepository repository;

    @Override
    public PaymentFailure save(PaymentFailure failure) {
        return repository.save(failure);
    }
}
