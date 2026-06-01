package com.example.demo.product.application.event;

import java.util.UUID;

public record ProductDeletedEvent(UUID productId) {
}
