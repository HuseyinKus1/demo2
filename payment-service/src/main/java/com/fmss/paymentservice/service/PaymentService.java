package com.fmss.paymentservice.service;

import com.fmss.paymentservice.dto.PaymentRequest;
import com.fmss.paymentservice.dto.PaymentResponse;
import com.fmss.paymentservice.mapper.PaymentMapper;
import com.fmss.paymentservice.model.Payment;
import com.fmss.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentResponse create(PaymentRequest paymentRequest) {
        if (paymentRequest.getBalance().compareTo(paymentRequest.getPrice()) >= 0) {
            return paymentMapper.toPaymentResponse(paymentRepository.save(paymentMapper.toPayment(paymentRequest)));
        }else throw new RuntimeException("Insufficient funds");
    }}

