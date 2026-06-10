package com.example.demo.search.infrastructure.dto;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Getter
@Document(indexName = "search-keywords")
public class SearchKeywordDocument {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String keyword;

    @Field(type = FieldType.Keyword)
    private String normalizedKeyword;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Instant searchedAt;

    public SearchKeywordDocument() {
    }

    public SearchKeywordDocument(String id, String keyword, String normalizedKeyword, Instant searchedAt) {
        this.id = id;
        this.keyword = keyword;
        this.normalizedKeyword = normalizedKeyword;
        this.searchedAt = searchedAt;
    }
}
