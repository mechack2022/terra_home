package com.fragile.terra_home.services;

import com.fragile.terra_home.constants.TransactionStatus;
import com.fragile.terra_home.dto.request.InitializePaymentDto;
import com.fragile.terra_home.dto.response.ApiResponse;
import com.fragile.terra_home.dto.response.InitializePaymentResponse;
import com.fragile.terra_home.dto.response.PaymentVerificationResponse;
import com.fragile.terra_home.entities.Goer;
import com.fragile.terra_home.entities.GoerPayLog;
import com.fragile.terra_home.exceptions.PayStackExcption;
import com.fragile.terra_home.exceptions.ResourceNotFoundException;
import com.fragile.terra_home.repository.GoerPaymentLogRepository;
import com.fragile.terra_home.repository.GoerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.fragile.terra_home.constants.ApiConstant.PAYSTACK_INITIALIZE_PAY;
import static com.fragile.terra_home.constants.ApiConstant.PAYSTACK_VERIFY;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaystackServiceImplementation implements PaystackServices {

    private final RestTemplate restTemplate;

    private final GoerRepository goerRepository;

    private final ApiCallServices apiCallServices;

    private final GoerPaymentLogRepository goerPaymentLogRepository;
    @Value("${paystack_secret_key}")
    private String payStackSecretKey;


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(payStackSecretKey);
        return headers;

    }

    @Override
    public ResponseEntity<?> initializePayment(Long goerId) {
        try {
            Optional<Goer> goer = goerRepository.findById(goerId);
            if (goer.isEmpty()) {
                return new ResponseEntity<>(ApiResponse.builder().message("Goer not found with id :" + goerId).status(false).data(null).build(), HttpStatus.BAD_REQUEST);
            }

             Map<String, String> req = new HashMap<>();
             req.put("amount", goer.get().getTicketTotalAmount().toString());
             req.put("email", goer.get().getEmail());

             ResponseEntity<InitializePaymentResponse> responseEntity = apiCallServices.initiateTransaction(PAYSTACK_INITIALIZE_PAY, req);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // create a new goer payment log
                GoerPayLog paymentLog = createGoerPaymentLog(goer.get(), Objects.requireNonNull(responseEntity.getBody()));

                return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
            } else {
                throw new PayStackExcption("Paystack is unable to initialize payment at the moment", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Internal server Error" + ex.getMessage(), ex);
        }

    }


    private GoerPayLog createGoerPaymentLog(Goer goer, InitializePaymentResponse initializePaymentResponse) {
        var res = initializePaymentResponse.getData();
        if (!Objects.isNull(res)) {
            GoerPayLog paymentLog = GoerPayLog.builder().transactionReference(res.getReference()).transactionDate(LocalDateTime.now().toString()).goer(goer).amount(goer.getTicketTotalAmount()).build();

            return goerPaymentLogRepository.save(paymentLog);
        }
        throw new ResourceNotFoundException("Payment Initialisation data  is null", HttpStatus.BAD_REQUEST);
    }


    @Override
    public PaymentVerificationResponse verifyPayment(Long goerId, String reference) {
        Goer goer = goerRepository.findById(goerId).orElseThrow(() -> new ResourceNotFoundException("Goer not found with this id : " + goerId));
        GoerPayLog paymentLog = goerPaymentLogRepository.findByGoer(goer).orElseThrow(() -> new ResourceNotFoundException("No PaymentLog for this goer"));
        try {
            if (paymentLog.getTransactionReference().equals(reference)) {
                // Define the URL with the reference as a path variable
                String url = PAYSTACK_VERIFY + reference;
                ResponseEntity<?> responseEntity = apiCallServices.verifyPaymentApi(url);

                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    PaymentVerificationResponse payStackResponse = (PaymentVerificationResponse) responseEntity.getBody();
                    // Update paymentLog status
                    paymentLog.setTransactionStatus(TransactionStatus.SUCCESS);
                    goerPaymentLogRepository.save(paymentLog);

                    return payStackResponse;
                } else {
                    throw new ResourceNotFoundException("Error Occurred while connecting to PayStack URL");
                }
            }
            throw new ResourceNotFoundException("Wrong payment reference : " + reference, HttpStatus.BAD_REQUEST);
        } catch (HttpClientErrorException e) {
            // Handle HttpClientErrorException for cases where the response status code is not OK (e.g., 404)
            throw new RuntimeException("Error Occurred while connecting to PayStack URL" + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }
}
