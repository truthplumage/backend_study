package com.example.demo.seller.application.usecase;

import com.example.demo.seller.domain.model.Seller;
import com.example.demo.seller.presentation.dto.SellerCreateRequest;
import com.example.demo.seller.presentation.dto.SellerUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface SellerUseCase {

    Seller create(SellerCreateRequest request);

    Seller getById(UUID sellerId);

    List<Seller> getAll();

    Seller update(UUID sellerId, SellerUpdateRequest request);

    void delete(UUID sellerId);
}
