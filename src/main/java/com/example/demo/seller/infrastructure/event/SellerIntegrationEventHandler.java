package com.example.demo.seller.infrastructure.event;

import com.example.demo.seller.application.event.SellerCreatedEvent;
import com.example.demo.seller.application.event.SellerDeletedEvent;
import com.example.demo.seller.application.event.SellerUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class SellerIntegrationEventHandler {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(SellerCreatedEvent event) {
        log.info("Seller created event handled after commit. sellerId={}", event.sellerId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(SellerUpdatedEvent event) {
        log.info("Seller updated event handled after commit. sellerId={}", event.sellerId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(SellerDeletedEvent event) {
        log.info("Seller deleted event handled after commit. sellerId={}", event.sellerId());
    }
}
