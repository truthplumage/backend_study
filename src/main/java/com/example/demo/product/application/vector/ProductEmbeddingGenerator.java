package com.example.demo.product.application.vector;

import java.util.Optional;

public interface ProductEmbeddingGenerator {

    Optional<float[]> generate(String text);
}
