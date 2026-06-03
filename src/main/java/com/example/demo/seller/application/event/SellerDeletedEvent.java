package com.example.demo.seller.application.event;

import java.util.UUID;

public record SellerDeletedEvent(UUID sellerId) {
}
