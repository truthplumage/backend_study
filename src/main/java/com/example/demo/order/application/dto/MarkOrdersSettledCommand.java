package com.example.demo.order.application.dto;

import java.util.List;
import java.util.UUID;

public record MarkOrdersSettledCommand(
        UUID settlementBatchId,
        List<UUID> orderIds,
        UUID actorId
) {
}
