package com.example.demo.product.domain.repository;

import com.example.demo.product.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID productId);

    List<Product> findAll();

    List<Product> findNearestProducts(float[] embedding, int limit);

    void delete(Product product);
}
