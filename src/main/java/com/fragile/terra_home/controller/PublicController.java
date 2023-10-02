package com.fragile.terra_home.controller;

import com.fragile.terra_home.constants.ApiConstant;
import com.fragile.terra_home.dto.response.ApiResponse;
import com.fragile.terra_home.entities.Event;
import com.fragile.terra_home.services.EventServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class PublicController {

    private final EventServices eventServices;

    @GetMapping("/home")
    public ResponseEntity<String> homePage() {
        return new ResponseEntity<>("Home page", HttpStatus.OK);
    }

    @GetMapping("/events")
    public ResponseEntity<ApiResponse<?>> getAllEvents() {
        List<Event> eventList = eventServices.getAllEvent();
        ApiResponse<?> apiResponse = ApiResponse
                .builder()
                .message(ApiConstant.IS_SUCCESS)
                .data(eventList)
                .status(true)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
