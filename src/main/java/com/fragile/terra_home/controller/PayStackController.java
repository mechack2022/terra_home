package com.fragile.terra_home.controller;

import com.fragile.terra_home.dto.request.InitializePaymentDto;
import com.fragile.terra_home.dto.response.InitializePaymentResponse;
import com.fragile.terra_home.services.PaystackServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/public/user")
@RestController
@RequiredArgsConstructor
public class PayStackController {
    private final PaystackServices paystackServices;

    @PostMapping("/initialize-payment")
    public ResponseEntity<InitializePaymentResponse> initializePayment(@Valid @RequestBody InitializePaymentDto req) {
        InitializePaymentResponse res = paystackServices.initializePayment(req);
        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

}
