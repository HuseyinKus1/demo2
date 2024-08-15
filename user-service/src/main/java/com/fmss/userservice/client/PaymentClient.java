package com.fmss.userservice.client;

import com.fmss.userservice.dto.request.PaymentRequest;
import com.fmss.userservice.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("api/v1/payments/process")
    ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest paymentRequest);


}