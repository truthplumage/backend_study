package com.example.demo.product.infrastructure.vector;

import com.example.demo.product.application.vector.ProductEmbeddingGenerator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NoOpProductEmbeddingGenerator implements ProductEmbeddingGenerator {

    @Override
    public Optional<float[]> generate(String text) {
        return Optional.empty();
    }
}
