package com.example.demo.order.application.usecase;

import com.example.demo.order.application.dto.CreateOrderCommand;
import com.example.demo.order.application.dto.MarkOrdersSettledCommand;
import com.example.demo.order.application.dto.MarkOrderPaidCommand;
import com.example.demo.order.application.dto.OrderResult;

import java.time.LocalDate;
import java.util.List;

public interface OrderUseCase {

    OrderResult create(CreateOrderCommand command);

    OrderResult markPaid(MarkOrderPaidCommand command);

    void markSettled(MarkOrdersSettledCommand command);

    List<OrderResult> findAll();

    List<OrderResult> findSettlementCandidates(LocalDate settlementDate);
}
