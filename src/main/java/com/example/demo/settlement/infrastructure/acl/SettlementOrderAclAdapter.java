package com.example.demo.settlement.infrastructure.acl;

import com.example.demo.order.domain.repository.OrderRepository;
import com.example.demo.settlement.application.acl.SettlementOrderAcl;
import com.example.demo.settlement.domain.model.SettlementOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SettlementOrderAclAdapter implements SettlementOrderAcl {

    private final OrderRepository orderRepository;
    private final SettlementOrderTranslator settlementOrderTranslator;

    @Override
    public List<SettlementOrder> findSettlementCandidates(LocalDate settlementDate) {
        LocalDate targetDate = settlementDate == null ? LocalDate.now() : settlementDate;
        LocalDateTime fromInclusive = targetDate.atStartOfDay();
        LocalDateTime toExclusive = fromInclusive.plusDays(1);

        return orderRepository.findUnsettledPaidOrders(fromInclusive, toExclusive).stream()
                .map(settlementOrderTranslator::translate)
                .toList();
    }
}
