package com.example.demo.product.adapter.out;

import com.example.demo.product.application.out.ProductPersistence;
import com.example.demo.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPersistence {
    private final ProductJpaRepository jpaRepository;
    @Override
    public Product save(Product product) {
        return jpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        return jpaRepository.findById(productId);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void delete(Product product) {
        jpaRepository.delete(product);
    }
}
