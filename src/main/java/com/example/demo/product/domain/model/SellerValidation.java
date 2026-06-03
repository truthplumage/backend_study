package com.example.demo.product.domain.model;

import java.util.UUID;

public record SellerValidation(
        UUID sellerId,
        String sellerName,
        String status
) {

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }
}
