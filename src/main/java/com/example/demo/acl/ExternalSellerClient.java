package com.example.demo.acl;

import org.springframework.stereotype.Component;

@Component
public class ExternalSellerClient {

    public ExternalSellerResponse getSeller(String sellerId) {
        return new ExternalSellerResponse(sellerId, "External Seller", "ACTIVE");
    }
}
