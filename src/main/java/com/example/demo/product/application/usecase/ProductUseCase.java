package com.example.demo.product.application.usecase;

import com.example.demo.product.domain.model.Product;
import com.example.demo.product.presentation.dto.ProductCreateRequest;
import com.example.demo.product.presentation.dto.ProductUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ProductUseCase {

    Product create(ProductCreateRequest request);

    Product getById(UUID productId);

    List<Product> getAll();

    Product update(UUID productId, ProductUpdateRequest request);

    void delete(UUID productId);
}
