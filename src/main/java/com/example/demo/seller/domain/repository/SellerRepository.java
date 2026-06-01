package com.example.demo.seller.domain.repository;

import com.example.demo.seller.domain.model.Seller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SellerRepository {

    Seller save(Seller seller);

    Optional<Seller> findById(UUID sellerId);

    List<Seller> findAll();

    void delete(Seller seller);
}
