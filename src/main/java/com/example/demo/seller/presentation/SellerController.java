package com.example.demo.seller.presentation;

import com.example.demo.seller.application.usecase.SellerCommandUseCase;
import com.example.demo.seller.application.usecase.SellerQueryUseCase;
import com.example.demo.seller.domain.model.Seller;
import com.example.demo.seller.presentation.dto.SellerCreateRequest;
import com.example.demo.seller.presentation.dto.SellerUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.init}/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerCommandUseCase sellerCommandUseCase;
    private final SellerQueryUseCase sellerQueryUseCase;

    @PostMapping
    @Operation(summary = "판매자 생성", description = "신규 판매자를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = Seller.class))),
            @ApiResponse(responseCode = "400", description = "요청 값 오류")
    })
    public ResponseEntity<Seller> create(@RequestBody SellerCreateRequest request) {
        Seller response = sellerCommandUseCase.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{sellerId}")
    @Operation(summary = "판매자 단건 조회", description = "판매자 ID로 판매자 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Seller.class))),
            @ApiResponse(responseCode = "404", description = "판매자 없음")
    })
    public Seller getById(@Parameter(description = "판매자 UUID") @PathVariable UUID sellerId) {
        return sellerQueryUseCase.getById(sellerId);
    }

    @GetMapping
    @Operation(summary = "판매자 목록 조회", description = "전체 판매자 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public List<Seller> getAll() {
        return sellerQueryUseCase.getAll();
    }

    @PutMapping("/{sellerId}")
    @Operation(summary = "판매자 수정", description = "판매자 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = Seller.class))),
            @ApiResponse(responseCode = "404", description = "판매자 없음")
    })
    public Seller update(@Parameter(description = "판매자 UUID") @PathVariable UUID sellerId,
                         @RequestBody SellerUpdateRequest request) {
        return sellerCommandUseCase.update(sellerId, request);
    }

    @DeleteMapping("/{sellerId}")
    @Operation(summary = "판매자 삭제", description = "판매자를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "판매자 없음")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "판매자 UUID") @PathVariable UUID sellerId) {
        sellerCommandUseCase.delete(sellerId);
        return ResponseEntity.noContent().build();
    }
}
