package com.fmss.paymentservice.mapper;

import com.fmss.paymentservice.dto.PaymentRequest;
import com.fmss.paymentservice.dto.PaymentResponse;
import com.fmss.paymentservice.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PaymentMapper {


   public Payment toPayment(PaymentRequest paymentRequest){
        Payment.PaymentBuilder payment = Payment.builder();
        payment.userId(paymentRequest.getUserId());
        payment.packagesId(paymentRequest.getPackagesId());
        payment.status("success");
        return payment.build();
    }

    public PaymentResponse toPaymentResponse(Payment payment){
       PaymentResponse paymentResponse = new PaymentResponse(payment.getId(), payment.getStatus());
       return paymentResponse;
    }
}
