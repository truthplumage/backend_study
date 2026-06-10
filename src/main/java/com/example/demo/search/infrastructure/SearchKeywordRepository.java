package com.example.demo.search.infrastructure;

import com.example.demo.search.infrastructure.dto.SearchKeywordDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchKeywordRepository extends ElasticsearchRepository<SearchKeywordDocument, String> {
}
