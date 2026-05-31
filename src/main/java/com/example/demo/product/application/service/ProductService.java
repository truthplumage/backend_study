package com.example.demo.product.application.service;

import com.example.demo.product.adapter.dto.ProductCreateRequest;
import com.example.demo.product.adapter.dto.ProductUpdateRequest;
import com.example.demo.product.domain.Product;
import com.example.demo.product.application.in.ProductUseCase;
import com.example.demo.product.application.out.ProductPersistence;
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

    private final ProductPersistence productPersistence;

    @Override
    @Transactional
    public Product create(ProductCreateRequest request) {
        Product product = Product.create(
                toUuid(request.sellerId(), "sellerId"),
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.status(),
                toUuid(request.creatorId(), "creatorId")
        );
        return productPersistence.save(product);
    }

    @Override
    public Product getById(UUID productId) {
        Product product = findByIdOrThrow(productId);
        return product;
    }

    @Override
    public List<Product> getAll() {
        return productPersistence.findAll();
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
        productPersistence.delete(product);
    }

    private Product findByIdOrThrow(UUID productId) {
        return productPersistence.findById(productId)
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
