package com.example.demo.seller.infrastructure.persistence;

import com.example.demo.seller.domain.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerJpaRepository extends JpaRepository<Seller, UUID> {
}
