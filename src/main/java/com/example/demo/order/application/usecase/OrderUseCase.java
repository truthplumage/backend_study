package com.example.demo.order.application.usecase;

import com.example.demo.order.application.dto.CreateOrderCommand;
import com.example.demo.order.application.dto.OrderResult;

import java.time.LocalDate;
import java.util.List;

public interface OrderUseCase {

    OrderResult create(CreateOrderCommand command);

    List<OrderResult> findAll();

    List<OrderResult> findSettlementCandidates(LocalDate settlementDate);
}
