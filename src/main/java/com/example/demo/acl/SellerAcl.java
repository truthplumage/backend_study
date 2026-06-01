package com.example.demo.acl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class SellerAcl {

    private final ExternalSellerClient externalSellerClient;
    private final SellerAclTranslator sellerAclTranslator;

    public SellerProfile getActiveSeller(String sellerId) {
        ExternalSellerResponse response = externalSellerClient.getSeller(sellerId);
        SellerProfile sellerProfile = sellerAclTranslator.translate(response);
        if (!sellerProfile.active()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seller is not active");
        }
        return sellerProfile;
    }
}
