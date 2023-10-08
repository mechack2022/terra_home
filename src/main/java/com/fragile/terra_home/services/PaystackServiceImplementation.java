package com.fragile.terra_home.services;

import com.fragile.terra_home.constants.TransactionStatus;
import com.fragile.terra_home.dto.request.InitializePaymentDto;
import com.fragile.terra_home.dto.response.ApiResponse;
import com.fragile.terra_home.dto.response.InitializePaymentResponse;
import com.fragile.terra_home.dto.response.PaymentVerificationResponse;
import com.fragile.terra_home.entities.Goer;
import com.fragile.terra_home.entities.GoerPaymentLog;
import com.fragile.terra_home.exceptions.PayStackExcption;
import com.fragile.terra_home.exceptions.ResourceNotFoundException;
import com.fragile.terra_home.repository.GoerPaymentLogRepository;
import com.fragile.terra_home.repository.GoerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.fragile.terra_home.constants.ApiConstant.PAYSTACK_INITIALIZE_PAY;
import static com.fragile.terra_home.constants.ApiConstant.PAYSTACK_VERIFY;

@Service
@RequiredArgsConstructor
public class PaystackServiceImplementation implements PaystackServices {

    private final RestTemplate restTemplate;

    private final GoerRepository goerRepository;

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
             if(goer.isEmpty()){
                 return new ResponseEntity<>(ApiResponse.builder()
                         .message("Goer not found with id :"+ goerId)
                         .status(false)
                         .data(null)
                         .build(), HttpStatus.BAD_REQUEST);
             }
            InitializePaymentDto initializePaymentDto = new InitializePaymentDto();
             initializePaymentDto.setAmount(goer.get().getTicketTotalAmount().toString());
             initializePaymentDto.setEmail(goer.get().getEmail());

            HttpEntity<InitializePaymentDto> requestEntity = new HttpEntity<>(initializePaymentDto, getHeaders());
            ResponseEntity<InitializePaymentResponse> responseEntity = restTemplate.postForEntity(
                    PAYSTACK_INITIALIZE_PAY, requestEntity, InitializePaymentResponse.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // create a new goer payment log
                GoerPaymentLog goerPaymentLog = createGoerPaymentLog(goer.get(), Objects.requireNonNull(responseEntity.getBody()));

                return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
            } else {
                throw new PayStackExcption("Paystack is unable to initialize payment at the moment", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Internal server Error" + ex.getMessage(), ex);
        }

    }

    @Override
    public ResponseEntity<?> verifyPayment(Long goerId, String reference){
        try{
           Goer goer = goerRepository.findById(goerId).orElseThrow(() -> new ResourceNotFoundException("Goer not found with this id : " + goerId));
           GoerPaymentLog goerPaymentLog = goerPaymentLogRepository.findByGoer(goer).orElseThrow(() -> new ResourceNotFoundException("No PaymentLog for this goer"));
           if(goerPaymentLog.getTransactionReference().equals(reference)){
               HttpEntity<?>  requestEntity = new HttpEntity<>(getHeaders());
               String url = PAYSTACK_VERIFY + reference;
               ResponseEntity<?> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, PaymentVerificationResponse.class);
               if(responseEntity.getStatusCode() == HttpStatus.OK){
//                   change the payment status
                   goerPaymentLog.setTransactionStatus(TransactionStatus.SUCCESS);
                   goerPaymentLogRepository.save(goerPaymentLog);
                 // create new paymentInformation
                 
                    return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK );
               }
               throw new RestClientException("RestTemplate error while verifying payment");
           }
           throw new ResourceNotFoundException("Wrong payment reference : "+ reference,HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            throw new RuntimeException("Internal server Error" + ex.getMessage(), ex);
        }
    }

    private GoerPaymentLog createGoerPaymentLog(Goer goer, InitializePaymentResponse initializePaymentResponse){
       var res =  initializePaymentResponse.getData();
       if(!Objects.isNull(res)){
           GoerPaymentLog goerPaymentLog = GoerPaymentLog.builder()
                   .transactionReference(res.getReference())
                   .transactionDate(LocalDateTime.now().toString())
                   .goer(goer)
                   .amount(goer.getTicketTotalAmount())
                   .build();

          return goerPaymentLogRepository.save(goerPaymentLog);
       }
       throw new ResourceNotFoundException("Payment Initialisation data  is null", HttpStatus.BAD_REQUEST);
    }




}
