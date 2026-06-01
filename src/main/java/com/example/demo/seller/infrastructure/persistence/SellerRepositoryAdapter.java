package com.example.demo.seller.infrastructure.persistence;

import com.example.demo.seller.domain.model.Seller;
import com.example.demo.seller.domain.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SellerRepositoryAdapter implements SellerRepository {

    private final SellerJpaRepository sellerJpaRepository;

    @Override
    public Seller save(Seller seller) {
        return sellerJpaRepository.save(seller);
    }

    @Override
    public Optional<Seller> findById(UUID sellerId) {
        return sellerJpaRepository.findById(sellerId);
    }

    @Override
    public List<Seller> findAll() {
        return sellerJpaRepository.findAll();
    }

    @Override
    public void delete(Seller seller) {
        sellerJpaRepository.delete(seller);
    }
}
