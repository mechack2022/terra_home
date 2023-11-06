package com.fragile.terra_home.services;

import com.fragile.terra_home.constants.ApiConstant;
import com.fragile.terra_home.constants.TransactionStatus;
import com.fragile.terra_home.dto.response.ApiResponse;
import com.fragile.terra_home.dto.response.InitializePaymentResponse;
import com.fragile.terra_home.dto.response.PaymentVerificationResponse;
import com.fragile.terra_home.entities.Event;
import com.fragile.terra_home.entities.Goer;
import com.fragile.terra_home.entities.GoerPayLog;
import com.fragile.terra_home.entities.Ticket;
import com.fragile.terra_home.exceptions.PayStackExcption;
import com.fragile.terra_home.exceptions.ResourceNotFoundException;
import com.fragile.terra_home.repository.EventRepository;
import com.fragile.terra_home.repository.GoerPaymentLogRepository;
import com.fragile.terra_home.repository.GoerRepository;
import com.fragile.terra_home.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;


import java.math.BigDecimal;
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
    private final EventRepository eventRepository;

    private final GoerRepository goerRepository;

    private final TicketRepository ticketRepository;

    private final ApiCallServices apiCallServices;

    private final GoerPaymentLogRepository goerPaymentLogRepository;


    @Override
    @Transactional
    public ResponseEntity<?> initializePayment(Long goerId) {
        Optional<GoerPayLog> foundPayLog = goerPaymentLogRepository.findByGoerId(goerId);

        if (foundPayLog.isPresent()) {
            throw new ResourceNotFoundException("Ticket payment is already initialized for this Goer", HttpStatus.CONFLICT);
        }

        Optional<Goer> goer = goerRepository.findById(goerId);
        if (goer.isEmpty()) {
            return new ResponseEntity<>(ApiResponse.builder()
                    .message("Goer not found with ID: " + goerId)
                    .status(false)
                    .data(null)
                    .build(), HttpStatus.BAD_REQUEST);
        }

        Map<String, String> req = new HashMap<>();
        req.put("email", goer.get().getEmail());
        req.put("amount", goer.get().getTicketTotalAmount().toString());

        log.info("Payment initialization data {}", req);

        ResponseEntity<InitializePaymentResponse> responseEntity = apiCallServices.initiateTransaction(PAYSTACK_INITIALIZE_PAY, req);
        log.debug("Response from Payment Initialization: " + responseEntity);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Create and save GoerPayLog
            createGoerPaymentLog(goer.get(), Objects.requireNonNull(responseEntity.getBody()).getData());

            return new ResponseEntity<>(responseEntity.getBody().getData(), HttpStatus.OK);
        } else {
            throw new PayStackExcption("Paystack is unable to initialize payment at the moment", HttpStatus.BAD_REQUEST);
        }
    }



    private void createGoerPaymentLog(Goer goer, InitializePaymentResponse.Data data) {
        assert data != null;
        GoerPayLog paymentLog = GoerPayLog.builder()
                .transactionReference(data.getReference())
                .transactionDate(LocalDateTime.now().toString())
                .goer(goer)
                .amount(goer.getTicketTotalAmount())
                .build();

        goerPaymentLogRepository.save(paymentLog);
    }


    @Override
    @Transactional
    public PaymentVerificationResponse verifyPayment(Long goerId, String reference) {
        Goer goer = goerRepository.findById(goerId).orElseThrow(() -> new ResourceNotFoundException("Goer not found with this id : " + goerId));
        GoerPayLog paymentLog = goerPaymentLogRepository.findByGoer(goer).orElseThrow(() -> new ResourceNotFoundException("No PaymentLog for this goer"));
//        check if transaction has been made for this goer ticket
        if (paymentLog.getTransactionStatus().equals(TransactionStatus.SUCCESS)) {
            throw new ResourceNotFoundException("Ticket transaction has been made for this ticketId " + goer.getTicket().getId(), HttpStatus.CONFLICT);
        }

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
//                  change goer paid status to paid
                    goer.setIsPaid(true);
                    goerRepository.save(goer);
                    // update event totalTicketSalePrice, NoOfTicketSales, eventCharges, if the goer payment has been verified
                    updateEvent(goer);

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

    // update event totalTicketSalePrice, NoOfTicketSales, eventCharges
    private void updateEvent(Goer goer) {
        if (goer.getIsPaid()) {
            Ticket ticket = ticketRepository.findById(goer.getTicket().getId()).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

            Event event = eventRepository.findById(ticket.getEvent().getId()).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
            Integer noOfTicketBought = goer.getNoOfTickets();
            Integer updatedTicketNo = event.getNoOfTicketSales() + noOfTicketBought;
            event.setNoOfTicketSales(updatedTicketNo);
//                    update the event charges for the particular event that the ticket payment was verified
            BigDecimal chargePerTicket = BigDecimal.valueOf(ApiConstant.PER_TICKET_CHARGE).multiply(BigDecimal.valueOf(ticket.getTicketPrice()));

            BigDecimal goerTicketCharge = chargePerTicket.multiply(BigDecimal.valueOf(noOfTicketBought));
            BigDecimal updatedEventCharges = event.getEventCharges().add(goerTicketCharge);
            event.setEventCharges(updatedEventCharges);
//                    update event total sales price for the particular event that the ticket payment was verified
            BigDecimal updatedTotalTicketSalesPrice = event.getTotalTicketSalePrice().add(goer.getTicketTotalAmount());
            event.setTotalTicketSalePrice(updatedTotalTicketSalesPrice);
            event.setWithdrawalAmount(updatedTotalTicketSalesPrice.subtract(updatedEventCharges));

            eventRepository.save(event);

        }

    }

//      private GoerPayLog isTransactionSuccess(Long goerId){
//          Goer goer = goerRepository.findById(goerId).orElseThrow(() -> new ResourceNotFoundException("Goer not found with this id : " + goerId));
//          GoerPayLog paymentLog = goerPaymentLogRepository.findByGoer(goer).orElseThrow(() -> new ResourceNotFoundException("No PaymentLog for this goer"));
////        check if transaction has been made for this goer ticket
//          if (paymentLog.getTransactionStatus().equals(TransactionStatus.SUCCESS)) {
//              throw new ResourceNotFoundException("Ticket transaction has been made for this ticketId " + goer.getTicket().getId(), HttpStatus.CONFLICT);
//          }
//          return paymentLog;
//     }
}
