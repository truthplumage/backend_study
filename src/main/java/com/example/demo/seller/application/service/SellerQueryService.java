package com.example.demo.seller.application.service;

import com.example.demo.seller.application.usecase.SellerQueryUseCase;
import com.example.demo.seller.domain.model.Seller;
import com.example.demo.seller.domain.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SellerQueryService implements SellerQueryUseCase {

    private final SellerRepository sellerRepository;

    @Override
    public Seller getById(UUID sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
    }

    @Override
    public List<Seller> getAll() {
        return sellerRepository.findAll();
    }
}
