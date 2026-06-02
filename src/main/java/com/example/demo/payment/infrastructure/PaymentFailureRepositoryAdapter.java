package com.example.demo.payment.infrastructure;

import com.example.demo.payment.domain.model.PaymentFailure;
import com.example.demo.payment.domain.repository.PaymentFailureRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentFailureRepositoryAdapter implements PaymentFailureRepository {

    private final PaymentFailureJpaRepository repository;

    public PaymentFailureRepositoryAdapter(PaymentFailureJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public PaymentFailure save(PaymentFailure failure) {
        return repository.save(failure);
    }
}
