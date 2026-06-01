package com.example.demo.event;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductCreatedEvent(UUID productId,
                                  String sellerId,
                                  String name,
                                  String description,
                                  BigDecimal price,
                                  Integer stock,
                                  String status,
                                  String creatorId) {
}
