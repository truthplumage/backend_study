package com.example.demo.search.infrastructure.event.dto;

public record ProductToSearch<T>(
        String eventType,
        T payload
) {
}
