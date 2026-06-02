package com.example.demo.payment.infrastructure.acl;

import com.example.demo.payment.application.dto.PaymentCommand;
import com.example.demo.payment.application.dto.PaymentConfirmation;
import com.example.demo.payment.client.TossPaymentClient;
import com.example.demo.payment.client.dto.TossPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TossPaymentAcl {

    private final TossPaymentClient tossPaymentClient;
    private final TossPaymentTranslator tossPaymentTranslator;

    public PaymentConfirmation confirm(PaymentCommand command) {
        TossPaymentResponse response = tossPaymentClient.confirm(command);
        return tossPaymentTranslator.translate(response);
    }
}
