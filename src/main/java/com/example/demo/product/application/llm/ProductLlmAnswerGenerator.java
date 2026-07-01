package com.example.demo.product.application.llm;

import com.example.demo.product.domain.model.Product;

import java.util.List;

public interface ProductLlmAnswerGenerator {

    String generateAnswer(String question, List<Product> products);
}
