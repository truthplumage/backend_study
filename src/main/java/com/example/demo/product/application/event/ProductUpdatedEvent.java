package com.example.demo.product.application.event;

import java.util.UUID;

public record ProductUpdatedEvent(UUID productId,
                                  String name,
                                  String brand,
                                  String category,
                                  java.math.BigDecimal price) {
}
