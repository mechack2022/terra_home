package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.InitializePaymentDto;
import com.fragile.terra_home.dto.response.InitializePaymentResponse;
import org.springframework.http.ResponseEntity;

public interface PaystackServices {

//    InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto);

    ResponseEntity<?> initializePayment(Long goerId);

    ResponseEntity<?> verifyPayment(Long goerId, String reference);
}
