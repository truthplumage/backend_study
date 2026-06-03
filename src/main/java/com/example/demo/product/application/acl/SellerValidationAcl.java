package com.example.demo.product.application.acl;

import com.example.demo.product.domain.model.SellerValidation;

import java.util.UUID;
//외부 통신을 위한 인터페이스
public interface SellerValidationAcl {

    SellerValidation validate(UUID sellerId);
}
