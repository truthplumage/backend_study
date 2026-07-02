package com.example.demo.product.infrastructure.event;

import com.example.demo.product.application.event.ProductCreatedEvent;
import com.example.demo.product.application.event.ProductDeletedEvent;
import com.example.demo.product.application.event.ProductUpdatedEvent;
import com.example.demo.product.infrastructure.event.dto.ProductSave;
import com.example.demo.product.infrastructure.event.dto.ProductToSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "kafka", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class ProductIntegrationEventHandler {
    @Value("search-service")
    private String topicName;
    private final KafkaTemplate<String, ProductToSearch> kafkaTemplate;
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductCreatedEvent event) {
        //검색에 제품 데이터를 입력하는 부분 추가 필요(kafka 통신을 통해서 동작되게 하여 의존성이 없도록 하는게 포인트).
        ProductToSearch<ProductSave> productToSearch = new ProductToSearch<>("create", new ProductSave(event.productId().toString(),
                event.name(), event.brand(), event.category(), event.price()));
        kafkaTemplate.send(topicName, productToSearch.payload().id(), productToSearch)
                .whenComplete((result, throwable) -> {
                    ProductSave productSave = (ProductSave) result.getProducerRecord().value().payload();
                    log.info("sended kafka : {}, {},{}"
                            , productSave.id()
                            , productSave.name()
                            , productSave.category());
                });
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductUpdatedEvent event) {
        //TODO: 검색에 제품 데이터를 수정하는 부분 추가 필요(kafka 통신을 통해서 동작되게 하여 의존성이 없도록 하는게 포인트).
        log.info("Product updated event handled after commit. productId={}", event.productId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductDeletedEvent event) {
        //TODO: 검색에 제품 데이터를 삭제하는 부분 추가 필요(kafka 통신을 통해서 동작되게 하여 의존성이 없도록 하는게 포인트).
        log.info("Product deleted event handled after commit. productId={}", event.productId());
    }
}
