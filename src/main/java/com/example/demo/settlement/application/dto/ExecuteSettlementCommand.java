package com.example.demo.settlement.application.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ExecuteSettlementCommand(
        LocalDate settlementDate,
        UUID actorId
) {
}
