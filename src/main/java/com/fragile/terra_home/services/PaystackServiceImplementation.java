package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.InitializePaymentDto;
import com.fragile.terra_home.dto.response.InitializePaymentResponse;
import com.fragile.terra_home.exceptions.PayStackExcption;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.fragile.terra_home.constants.ApiConstant.PAYSTACK_INITIALIZE_PAY;

@Service
@RequiredArgsConstructor
public class PaystackServiceImplementation implements PaystackServices {

    private final RestTemplate restTemplate;
    @Value("${paystack_secret_key}")
    private String payStackSecretKey;


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(payStackSecretKey);
        return headers;

    }

    @Override
    public InitializePaymentResponse initializePayment(InitializePaymentDto initializePaymentDto) {
        try {
            HttpEntity<InitializePaymentDto> requestEntity = new HttpEntity<>(initializePaymentDto, getHeaders());
            ResponseEntity<InitializePaymentResponse> responseEntity = restTemplate.postForEntity(
                    PAYSTACK_INITIALIZE_PAY, requestEntity, InitializePaymentResponse.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                throw new PayStackExcption("Paystack is unable to initialize payment at the moment", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Internal server Error" + ex.getMessage(), ex);
        }

    }


}
