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

    @PostMapping("/initialize-payment/{goerId}")
    public ResponseEntity<?> initializePayment(@PathVariable("goerId") Long goerId) {
        var res = paystackServices.initializePayment(goerId);
        return new ResponseEntity<>(res.getBody(), HttpStatus.CREATED);
    }

    @PostMapping("/verify-payment/{goerId}")
    public ResponseEntity<?> initializePayment(@PathVariable("goerId") Long goerId, @RequestParam(value = "reference") String reference) {
        var res = paystackServices.verifyPayment(goerId, reference);
        return new ResponseEntity<>(res.getBody(), HttpStatus.CREATED);
    }

}
