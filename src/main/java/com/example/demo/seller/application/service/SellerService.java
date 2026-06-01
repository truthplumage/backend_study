package com.example.demo.seller.application.service;

import com.example.demo.seller.adapter.in.web.dto.SellerCreateRequest;
import com.example.demo.seller.adapter.in.web.dto.SellerUpdateRequest;
import com.example.demo.seller.application.port.in.SellerUseCase;
import com.example.demo.seller.application.port.out.SellerRepositoryPort;
import com.example.demo.seller.domain.model.Seller;
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
public class SellerService implements SellerUseCase {

    private final SellerRepositoryPort sellerRepositoryPort;

    @Override
    @Transactional
    public Seller create(SellerCreateRequest request) {
        Seller seller = Seller.create(
                request.email(),
                request.name(),
                request.businessNumber(),
                request.status(),
                toUuid(request.creatorId(), "creatorId")
        );
        return sellerRepositoryPort.save(seller);
    }

    @Override
    public Seller getById(UUID sellerId) {
        return findByIdOrThrow(sellerId);
    }

    @Override
    public List<Seller> getAll() {
        return sellerRepositoryPort.findAll();
    }

    @Override
    @Transactional
    public Seller update(UUID sellerId, SellerUpdateRequest request) {
        Seller seller = findByIdOrThrow(sellerId);
        seller.update(
                request.email(),
                request.name(),
                request.businessNumber(),
                request.status(),
                toUuid(request.modifierId(), "modifierId")
        );
        return seller;
    }

    @Override
    @Transactional
    public void delete(UUID sellerId) {
        Seller seller = findByIdOrThrow(sellerId);
        sellerRepositoryPort.delete(seller);
    }

    private Seller findByIdOrThrow(UUID sellerId) {
        return sellerRepositoryPort.findById(sellerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
    }

    private UUID toUuid(String value, String fieldName) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " must be valid UUID");
        }
    }
}
