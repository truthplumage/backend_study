package com.example.demo.seller.application.port.in;

import com.example.demo.seller.adapter.in.web.dto.SellerCreateRequest;
import com.example.demo.seller.adapter.in.web.dto.SellerUpdateRequest;
import com.example.demo.seller.domain.model.Seller;

import java.util.List;
import java.util.UUID;

public interface SellerUseCase {

    Seller create(SellerCreateRequest request);

    Seller getById(UUID sellerId);

    List<Seller> getAll();

    Seller update(UUID sellerId, SellerUpdateRequest request);

    void delete(UUID sellerId);
}
