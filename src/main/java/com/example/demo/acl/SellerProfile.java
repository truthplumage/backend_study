package com.example.demo.acl;

import java.util.UUID;

public record SellerProfile(
        UUID sellerId,
        String sellerName,
        boolean active
) {
}
