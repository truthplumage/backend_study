package com.example.demo.payment.infrastructure.acl;

import com.example.demo.payment.application.dto.PaymentConfirmation;
import com.example.demo.payment.client.dto.TossPaymentResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TossPaymentTranslator {

    public PaymentConfirmation translate(TossPaymentResponse response) {
        LocalDateTime approvedAt = response.approvedAt() != null ? response.approvedAt().toLocalDateTime() : null;
        LocalDateTime requestedAt = response.requestedAt() != null ? response.requestedAt().toLocalDateTime() : null;

        return new PaymentConfirmation(
                response.paymentKey(),
                response.orderId(),
                response.totalAmount(),
                response.method(),
                approvedAt,
                requestedAt
        );
    }
}
