package com.example.demo.seller.application.usecase;

import com.example.demo.seller.domain.model.Seller;

import java.util.List;
import java.util.UUID;

public interface SellerQueryUseCase {

    Seller getById(UUID sellerId);

    List<Seller> getAll();
}
