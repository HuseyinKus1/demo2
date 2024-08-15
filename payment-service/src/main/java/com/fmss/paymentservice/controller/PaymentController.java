package com.fmss.paymentservice.controller;

import com.fmss.paymentservice.dto.PaymentRequest;
import com.fmss.paymentservice.dto.PaymentResponse;
import com.fmss.paymentservice.model.Payment;
import com.fmss.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> process(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.ok(paymentService.create(paymentRequest));
    }
}
