package com.example.demo.payment.application;

import com.example.demo.payment.application.dto.PaymentCommand;
import com.example.demo.payment.application.dto.PaymentFailCommand;
import com.example.demo.payment.application.dto.PaymentFailureInfo;
import com.example.demo.payment.application.dto.PaymentInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentUsecase {
    ResponseEntity<List<PaymentInfo>> findAll(Pageable pageable);
    ResponseEntity<PaymentInfo> confirm(PaymentCommand command);
    ResponseEntity<PaymentFailureInfo> recordFailure(PaymentFailCommand command);
}
