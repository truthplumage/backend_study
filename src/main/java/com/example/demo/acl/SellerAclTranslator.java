package com.example.demo.acl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class SellerAclTranslator {

    public SellerProfile translate(ExternalSellerResponse response) {
        UUID sellerId = toUuid(response.sellerNo());
        boolean active = "ACTIVE".equalsIgnoreCase(response.accountStatus());
        return new SellerProfile(sellerId, response.displayName(), active);
    }

    private UUID toUuid(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sellerId must be valid UUID");
        }
    }
}
