package com.example.demo.event;

import java.util.UUID;

public record ProductDeletedEvent(UUID productId) {
}
