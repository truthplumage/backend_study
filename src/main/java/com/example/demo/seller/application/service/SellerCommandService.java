package com.example.demo.seller.application.service;

import com.example.demo.seller.application.event.SellerCreatedEvent;
import com.example.demo.seller.application.event.SellerDeletedEvent;
import com.example.demo.seller.application.event.SellerUpdatedEvent;
import com.example.demo.seller.application.usecase.SellerCommandUseCase;
import com.example.demo.seller.domain.model.Seller;
import com.example.demo.seller.domain.repository.SellerRepository;
import com.example.demo.seller.presentation.dto.SellerCreateRequest;
import com.example.demo.seller.presentation.dto.SellerUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SellerCommandService implements SellerCommandUseCase {

    private final SellerRepository sellerRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Seller create(SellerCreateRequest request) {
        Seller seller = Seller.create(
                request.email(),
                request.name(),
                request.businessNumber(),
                request.status(),
                toUuid(request.creatorId(), "creatorId")
        );
        Seller savedSeller = sellerRepository.save(seller);
        eventPublisher.publishEvent(new SellerCreatedEvent(savedSeller.getId()));
        return savedSeller;
    }

    @Override
    public Seller update(UUID sellerId, SellerUpdateRequest request) {
        Seller seller = findByIdOrThrow(sellerId);
        seller.update(
                request.email(),
                request.name(),
                request.businessNumber(),
                request.status(),
                toUuid(request.modifierId(), "modifierId")
        );
        eventPublisher.publishEvent(new SellerUpdatedEvent(seller.getId()));
        return seller;
    }

    @Override
    public void delete(UUID sellerId) {
        Seller seller = findByIdOrThrow(sellerId);
        sellerRepository.delete(seller);
        eventPublisher.publishEvent(new SellerDeletedEvent(sellerId));
    }

    private Seller findByIdOrThrow(UUID sellerId) {
        return sellerRepository.findById(sellerId)
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
