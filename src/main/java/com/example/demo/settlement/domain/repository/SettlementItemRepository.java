package com.example.demo.settlement.domain.repository;

import com.example.demo.settlement.domain.model.SettlementItem;
import com.example.demo.settlement.domain.model.SettlementItemTotals;

import java.util.List;
import java.util.UUID;

public interface SettlementItemRepository {

    List<SettlementItem> saveAll(List<SettlementItem> items);

    SettlementItemTotals sumByBatchId(UUID batchId);
}
