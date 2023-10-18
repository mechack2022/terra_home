package com.fragile.terra_home.services;

import com.fragile.terra_home.constants.TransactionStatus;
import com.fragile.terra_home.dto.response.InitializePaymentResponse;
import com.fragile.terra_home.dto.response.PaymentVerificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiCallServices {

    private final RestTemplate restTemplate;

    @Value("${paystack_secret_key}")
    private String payStackSecretKey;

    public ResponseEntity<?> verifyPaymentApi(String url) {
        HttpEntity<?> requestEntity = new HttpEntity<>(getHeaders());
        try {
            return restTemplate.exchange(url, HttpMethod.GET, requestEntity, PaymentVerificationResponse.class);
            }catch(RestClientException e){
                log.info("error :{}", e.getMessage());
                throw new RuntimeException("Internal Server Error " + e.getMessage(), e);
            }
        }

    public ResponseEntity<InitializePaymentResponse> initiateTransaction(String url, Map<String, String> map) {
        HttpEntity<?> requestEntity = new HttpEntity<>(map, getHeaders());
        try {
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, InitializePaymentResponse.class);
        }catch(RestClientException e){
            throw new RuntimeException("Internal Server Error " + e.getMessage(), e);
        }
    }


    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(payStackSecretKey);
        return headers;

    }


}
