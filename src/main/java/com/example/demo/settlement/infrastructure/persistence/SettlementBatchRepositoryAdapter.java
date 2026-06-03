package com.example.demo.settlement.infrastructure.persistence;

import com.example.demo.settlement.domain.model.SettlementBatch;
import com.example.demo.settlement.domain.repository.SettlementBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SettlementBatchRepositoryAdapter implements SettlementBatchRepository {

    private final SettlementBatchJpaRepository settlementBatchJpaRepository;

    @Override
    public SettlementBatch save(SettlementBatch settlementBatch) {
        return settlementBatchJpaRepository.save(settlementBatch);
    }

    @Override
    public List<SettlementBatch> findAll() {
        return settlementBatchJpaRepository.findAll();
    }

    @Override
    public Optional<SettlementBatch> findById(UUID id) {
        return settlementBatchJpaRepository.findById(id);
    }
}
