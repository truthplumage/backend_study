package com.example.demo.product.application.service;

import com.example.demo.product.application.llm.ProductLlmAnswerGenerator;
import com.example.demo.product.application.vector.ProductEmbeddingService;
import com.example.demo.product.domain.model.Product;
import com.example.demo.product.domain.repository.ProductRepository;
import com.example.demo.product.presentation.dto.response.ProductLlmSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ProductApplicationService {

    private final ProductRepository productRepository;
    private final ProductEmbeddingService productEmbeddingService;
    private final ProductLlmAnswerGenerator productLlmAnswerGenerator;

    @Value("${openai.chat.enabled:false}")
    private boolean chatEnabled;

    public List<Product> semanticSearch(String query, int size) {
        int limit = normalizeSize(size);
        Optional<float[]> queryEmbedding = productEmbeddingService.embed(query);

        if (queryEmbedding.isPresent()) {
            List<Product> nearestProducts = productRepository.findNearestProducts(queryEmbedding.get(), limit);
            if (!nearestProducts.isEmpty()) {
                return nearestProducts;
            }
        }

        return lexicalSearch(query, limit);
    }

    @Transactional
    public void refreshEmbeddings() {
        productEmbeddingService.refreshEmbeddings();
    }

    public ProductLlmSearchResponse llmSearch(String question, int size) {
        String safeQuestion = question == null ? "" : question;
        List<Product> products = semanticSearch(safeQuestion, size);
        String answer = chatEnabled ? generateAnswer(safeQuestion, products) : fallbackAnswer(products);
        return new ProductLlmSearchResponse(safeQuestion, answer, products);
    }

    private String generateAnswer(String question, List<Product> products) {
        String answer = productLlmAnswerGenerator.generateAnswer(question, products);
        if (answer == null || answer.isBlank()) {
            return fallbackAnswer(products);
        }
        return answer;
    }

    private String fallbackAnswer(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return "관련 상품을 찾지 못했습니다.";
        }
        return "검색된 상품을 기준으로 추천했습니다. 아래 목록을 확인해 주세요.";
    }

    private int normalizeSize(int size) {
        return size <= 0 ? 5 : size;
    }

    private List<Product> lexicalSearch(String query, int limit) {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return List.of();
        }

        return products.stream()
                .sorted(Comparator
                        .comparingDouble((Product product) -> lexicalScore(product, query))
                        .reversed()
                        .thenComparing(Product::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .limit(limit)
                .toList();
    }

    private double lexicalScore(Product product, String query) {
        if (query == null || query.isBlank()) {
            return 0.0;
        }

        String haystack = ((product.getName() == null ? "" : product.getName()) + " "
                + (product.getDescription() == null ? "" : product.getDescription()))
                .toLowerCase(Locale.ROOT);
        String needle = query.toLowerCase(Locale.ROOT).trim();

        if (haystack.contains(needle)) {
            return 1.0;
        }

        Set<String> tokens = tokenize(needle);
        if (tokens.isEmpty()) {
            return 0.0;
        }

        long matches = tokens.stream().filter(haystack::contains).count();
        return (double) matches / tokens.size();
    }

    private Set<String> tokenize(String text) {
        return Arrays.stream(text.split("\\s+"))
                .map(String::trim)
                .filter(token -> !token.isBlank())
                .collect(Collectors.toSet());
    }
}
