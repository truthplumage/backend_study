package com.example.demo.event;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductUpdatedEvent(UUID productId,
                                  String name,
                                  String description,
                                  BigDecimal price,
                                  Integer stock,
                                  String status,
                                  String modifierId) {
}
