package com.example.demo.search.application;

import com.example.demo.search.infrastructure.dto.ProductDocument;
import com.example.demo.search.presentation.dto.request.IndexConfigRequest;
import com.example.demo.search.presentation.dto.request.ProductIndexRequest;
import com.example.demo.search.presentation.dto.response.IndexStatusResponse;
import com.example.demo.search.presentation.dto.response.IndexUpdateResponse;
import com.example.demo.search.presentation.dto.response.ProductSearchResponse;
import org.springframework.data.domain.Pageable;

public interface SearchUsecase {
    ProductDocument indexProduct(ProductIndexRequest request);
    IndexUpdateResponse applyProductIndexConfig(IndexConfigRequest request);
    IndexStatusResponse getProductIndexStatus();
    ProductSearchResponse searchProducts(String keyword, String category, Pageable pageable);
}
