package com.example.demo.seller.domain.model;

public record BusinessVerification(
        String businessNumber,
        String companyName,
        boolean valid
) {
}
