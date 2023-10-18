package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.InitializePaymentDto;
import com.fragile.terra_home.dto.response.InitializePaymentResponse;
import com.fragile.terra_home.dto.response.PaymentVerificationResponse;
import org.springframework.http.ResponseEntity;

public interface PaystackServices {
    ResponseEntity<?> initializePayment(Long goerId);


    PaymentVerificationResponse verifyPayment(Long goerId, String reference);

//    InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto);


}
