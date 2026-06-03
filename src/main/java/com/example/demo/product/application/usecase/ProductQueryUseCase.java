package com.example.demo.product.application.usecase;

import com.example.demo.product.domain.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductQueryUseCase {

    Product getById(UUID productId);

    List<Product> getAll();
}
