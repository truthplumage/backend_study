package com.example.demo.product.application.service;

import com.example.demo.product.application.usecase.ProductUseCase;
import com.example.demo.product.domain.exception.ProductNotFoundException;
import com.example.demo.product.domain.model.Product;
import com.example.demo.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product create(CreateProductCommand command) {
        Product product = Product.create(
                toUuid(command.sellerId(), "sellerId"),
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                command.status(),
                toUuid(command.creatorId(), "creatorId")
        );
        return productRepository.save(product);
    }

    @Override
    public Product getById(UUID productId) {
        return findByIdOrThrow(productId);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product update(UUID productId, UpdateProductCommand command) {
        Product product = findByIdOrThrow(productId);
        product.update(
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                command.status(),
                toUuid(command.modifierId(), "modifierId")
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
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    private UUID toUuid(String value, String fieldName) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(fieldName + " must be valid UUID");
        }
    }
}
