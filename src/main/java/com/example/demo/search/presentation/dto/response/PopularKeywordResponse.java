package com.example.demo.search.presentation.dto.response;

import java.util.List;

public record PopularKeywordResponse(
        List<PopularKeywordItemResponse> items
) {
}
