package com.example.demo.service;

import com.example.demo.dto.SellerCreateRequest;
import com.example.demo.dto.SellerUpdateRequest;
import com.example.demo.entity.Seller;

import java.util.List;
import java.util.UUID;

public interface SellerService {

    Seller create(SellerCreateRequest request);

    Seller getById(UUID sellerId);

    List<Seller> getAll();

    Seller update(UUID sellerId, SellerUpdateRequest request);

    void delete(UUID sellerId);
}
