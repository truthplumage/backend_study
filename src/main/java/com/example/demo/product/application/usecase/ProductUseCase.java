package com.example.demo.product.application.usecase;

import com.example.demo.product.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductUseCase {

    Product create(CreateProductCommand command);

    Product getById(UUID productId);

    List<Product> getAll();

    Product update(UUID productId, UpdateProductCommand command);

    void delete(UUID productId);

    record CreateProductCommand(
            String sellerId,
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            String status,
            String creatorId
    ) {
    }

    record UpdateProductCommand(
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            String status,
            String modifierId
    ) {
    }
}
