package com.example.demo.product.infrastructure.event.dto;

public record ProductToSearch<T>(
        String eventType,
        T payload
) {
}
