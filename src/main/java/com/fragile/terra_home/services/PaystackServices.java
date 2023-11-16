package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.InitializePaymentDto;
import com.fragile.terra_home.dto.response.InitializePaymentResponse;
import com.fragile.terra_home.dto.response.PaymentVerificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface PaystackServices {
    ResponseEntity<?> initializePayment(Long goerId);


    PaymentVerificationResponse verifyPayment(Long goerId, String reference);

    @Transactional
    ResponseEntity<?> creditAllCreatorsWithEventClosed();

//    InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto);


}
