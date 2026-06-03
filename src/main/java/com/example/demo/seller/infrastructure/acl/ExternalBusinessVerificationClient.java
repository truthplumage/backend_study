package com.example.demo.seller.infrastructure.acl;

import org.springframework.stereotype.Component;

@Component
public class ExternalBusinessVerificationClient {

    public ExternalBusinessVerificationResponse verify(String businessNumber) {
        return new ExternalBusinessVerificationResponse(
                businessNumber,
                "Verified Company",
                "NORMAL"
        );
    }
}
