package com.fragile.terra_home.controller;

import com.fragile.terra_home.constants.ApiConstant;
import com.fragile.terra_home.dto.response.ApiResponse;
import com.fragile.terra_home.dto.response.TicketResponseDto;
import com.fragile.terra_home.entities.Ticket;
import com.fragile.terra_home.entities.User;
import com.fragile.terra_home.services.CreatorService;
import com.fragile.terra_home.services.TicketServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/events/ticket")
public class TicketController {

    private final TicketServices ticketServices;

    private  final CreatorService creatorService;

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<?>> getCreatorTicketsByEventId(@RequestHeader("Authorization") String jwt, @PathVariable("eventId") Long eventId){
       User user = creatorService.findUserByJwt(jwt);
      List<TicketResponseDto> tickets =  ticketServices.findCreatorTicketsByEventId(user, eventId);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(ApiConstant.IS_SUCCESS)
                .status(true)
                .data(tickets)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
