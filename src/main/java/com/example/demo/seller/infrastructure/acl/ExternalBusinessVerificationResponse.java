package com.example.demo.seller.infrastructure.acl;

public record ExternalBusinessVerificationResponse(
        String bizNo,
        String corpNm,
        String taxType
) {
}
