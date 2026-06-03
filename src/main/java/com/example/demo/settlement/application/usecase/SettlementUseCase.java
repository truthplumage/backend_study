package com.example.demo.settlement.application.usecase;

import com.example.demo.settlement.application.dto.ExecuteSettlementCommand;
import com.example.demo.settlement.application.dto.SettlementBatchResult;

import java.util.List;

public interface SettlementUseCase {

    SettlementBatchResult execute(ExecuteSettlementCommand command);

    List<SettlementBatchResult> findAll();
}
