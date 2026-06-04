package com.example.demo.settlement.infrastructure.persistence;

import com.example.demo.settlement.domain.model.SettlementItem;
import com.example.demo.settlement.domain.model.SettlementItemTotals;
import com.example.demo.settlement.domain.repository.SettlementItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SettlementItemRepositoryAdapter implements SettlementItemRepository {

    private final SettlementItemJpaRepository settlementItemJpaRepository;

    @Override
    public List<SettlementItem> saveAll(List<SettlementItem> items) {
        return settlementItemJpaRepository.saveAll(items);
    }

    @Override
    public SettlementItemTotals sumByBatchId(UUID batchId) {
        SettlementItemTotalProjection projection = settlementItemJpaRepository.sumByBatchId(batchId);
        return new SettlementItemTotals(
                projection.getTotalGrossAmount(),
                projection.getTotalFeeAmount(),
                projection.getTotalRefundAmount(),
                projection.getTotalSettlementAmount()
        );
    }
}
