package com.example.demo.search.application;

import com.example.demo.search.infrastructure.dto.ProductDocument;
import com.example.demo.search.presentation.dto.request.IndexConfigRequest;
import com.example.demo.search.presentation.dto.request.ProductIndexRequest;
import com.example.demo.search.presentation.dto.response.IndexStatusResponse;
import com.example.demo.search.presentation.dto.response.IndexUpdateResponse;
import com.example.demo.search.presentation.dto.response.PopularKeywordResponse;
import com.example.demo.search.presentation.dto.response.ProductFilterAggregationResponse;
import com.example.demo.search.presentation.dto.response.ProductSearchResponse;
import com.example.demo.search.presentation.dto.response.ProductSuggestResponse;
import org.springframework.data.domain.Pageable;

public interface SearchUsecase {
    ProductDocument indexProduct(ProductIndexRequest request);
    IndexUpdateResponse applyProductIndexConfig(IndexConfigRequest request);
    IndexStatusResponse getProductIndexStatus();
    ProductSearchResponse searchProducts(String keyword, String category, Pageable pageable);
    ProductSuggestResponse suggestProducts(String keyword, int size);
    ProductFilterAggregationResponse aggregateProductFilters(String keyword);
    void recordKeyword(String keyword);
    PopularKeywordResponse getPopularKeywords(int days, int size);
}
