package com.example.demo.settlement.infrastructure.acl;

import com.example.demo.order.domain.model.Order;
import com.example.demo.settlement.domain.model.SettlementOrder;
import org.springframework.stereotype.Component;

@Component
public class SettlementOrderTranslator {

    public SettlementOrder translate(Order order) {
        return new SettlementOrder(
                order.getId(),
                order.getOrderNo(),
                order.getSellerId(),
                order.getGrossAmount(),
                order.getFeeAmount(),
                order.getRefundAmount(),
                order.getNetAmount(),
                order.getPaidAt()
        );
    }
}
