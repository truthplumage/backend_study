package com.example.demo.product.application.service;

import com.example.demo.product.application.acl.SellerValidationAcl;
import com.example.demo.product.application.usecase.ProductUseCase;
import com.example.demo.product.domain.model.Product;
import com.example.demo.product.domain.model.SellerValidation;
import com.example.demo.product.domain.repository.ProductRepository;
import com.example.demo.product.presentation.dto.ProductCreateRequest;
import com.example.demo.product.presentation.dto.ProductUpdateRequest;
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
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final SellerValidationAcl sellerValidationAcl;

    @Override
    @Transactional
    public Product create(ProductCreateRequest request) {
        SellerValidation sellerValidation = sellerValidationAcl.validate(toUuid(request.sellerId(), "sellerId"));
        if (!sellerValidation.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seller is not active");
        }
        Product product = Product.create(
                sellerValidation.sellerId(),
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.status(),
                toUuid(request.creatorId(), "creatorId")
        );
        return productRepository.save(product);
    }

    @Override
    public Product getById(UUID productId) {
        Product product = findByIdOrThrow(productId);
        return product;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product update(UUID productId, ProductUpdateRequest request) {
        Product product = findByIdOrThrow(productId);
        product.update(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.status(),
                toUuid(request.modifierId(), "modifierId")
        );
        return product;
    }

    @Override
    @Transactional
    public void delete(UUID productId) {
        Product product = findByIdOrThrow(productId);
        productRepository.delete(product);
    }

    private Product findByIdOrThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    private UUID toUuid(String value, String fieldName) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " must be valid UUID");
        }
    }
}
