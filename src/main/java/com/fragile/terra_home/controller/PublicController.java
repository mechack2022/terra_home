package com.fragile.terra_home.controller;

import com.fragile.terra_home.constants.ApiConstant;
import com.fragile.terra_home.dto.response.ApiResponse;
import com.fragile.terra_home.entities.Event;
import com.fragile.terra_home.services.EventServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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

    @PostMapping("/events")
    public ResponseEntity<ApiResponse<?>> filterEventsByCategoryOrLocationOrDate(
            @RequestParam(value = "category_name", required = false) String categoryName,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "date", required = false) String dateString) {
        try {
            LocalDateTime date = null;

            if (dateString != null) {
                date = LocalDateTime.parse(dateString);
            }

            List<Event> eventList = eventServices.filterEvent(categoryName, location, date);
            ApiResponse<?> apiResponse = ApiResponse
                    .builder()
                    .message(ApiConstant.IS_SUCCESS)
                    .data(eventList)
                    .status(true)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (DateTimeParseException ex) {
            // Handle invalid date format here
            ApiResponse<?> errorResponse = ApiResponse
                    .builder()
                    .message("Invalid date format")
                    .status(false)
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }


}
