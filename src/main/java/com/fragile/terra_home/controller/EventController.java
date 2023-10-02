package com.fragile.terra_home.controller;

import com.fragile.terra_home.config.JwtProvider;
import com.fragile.terra_home.constants.ApiConstant;
import com.fragile.terra_home.dto.request.CreateEventRequest;
import com.fragile.terra_home.dto.response.ApiResponse;
import com.fragile.terra_home.entities.Event;
import com.fragile.terra_home.entities.User;
import com.fragile.terra_home.services.CreatorService;
import com.fragile.terra_home.services.EventServices;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/events")
public class EventController {

    private final EventServices eventServices;

    private final CreatorService creatorService;


    @PostMapping
    public ResponseEntity<ApiResponse<?>> handleCreateEvent(@RequestHeader("Authorization") String jwt, @Valid @RequestBody CreateEventRequest req) throws UsernameNotFoundException {
        User user = creatorService.findUserByJwt(jwt);
        Event event = eventServices.createEvent(user, req);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(ApiConstant.IS_SUCCESS)
                .status(true)
                .data(event)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> handleCreateEvent(@RequestHeader("Authorization") String jwt) throws UsernameNotFoundException {
        User user = creatorService.findUserByJwt(jwt);
        List<Event> events = eventServices.getEventByCreator(user);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(ApiConstant.IS_SUCCESS)
                .status(true)
                .data(events)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponse<?>> updateEvent(@RequestHeader("Authorization") String jwt,
                                                      @PathVariable("eventId") Long eventId,
                                                      @Valid @RequestBody CreateEventRequest req) {
        User user = creatorService.findUserByJwt(jwt);
        Event updatedEvent = eventServices.updateEvent(eventId, user, req);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message("Event updated Successfully")
                .status(true)
                .data(updatedEvent)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }



}