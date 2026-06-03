package com.example.demo.payment.domain.repository;


import com.example.demo.payment.domain.model.PaymentFailure;

public interface PaymentFailureRepository {

    PaymentFailure save(PaymentFailure failure);
}
