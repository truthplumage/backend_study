package com.example.demo.service;

import com.example.demo.dto.ProductCreateRequest;
import com.example.demo.dto.ProductUpdateRequest;
import com.example.demo.entity.Product;

import java.util.UUID;

public interface ProductCommandUseCase {

    Product create(ProductCreateRequest request);

    Product update(UUID productId, ProductUpdateRequest request);

    void delete(UUID productId);
}
