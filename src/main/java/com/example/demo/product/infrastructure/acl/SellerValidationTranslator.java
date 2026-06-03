package com.example.demo.product.infrastructure.acl;

import com.example.demo.product.domain.model.SellerValidation;
import com.example.demo.seller.domain.model.Seller;
import org.springframework.stereotype.Component;

@Component
public class SellerValidationTranslator {

    public SellerValidation translate(Seller seller) {
        return new SellerValidation(
                seller.getId(),
                seller.getName(),
                seller.getStatus()
        );
    }
}
