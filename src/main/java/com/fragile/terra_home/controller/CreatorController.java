package com.fragile.terra_home.controller;

import com.fragile.terra_home.config.JwtProvider;
import com.fragile.terra_home.constants.ApiConstant;
import com.fragile.terra_home.dto.response.ApiResponse;
import com.fragile.terra_home.entities.User;
import com.fragile.terra_home.exceptions.UserException;
import com.fragile.terra_home.services.CreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/creator")
public class CreatorController {

    private final CreatorService creatorService;

    private final JwtProvider jwtProvider;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getCreatorProfile(@RequestHeader("Authorization") String jwt) throws UserException {
        try {
            String email = jwtProvider.getEmailFromToken(jwt);
            User userProfile = creatorService.getCreatorProfile(email);

            if (userProfile == null) {
                // Handle the case where the profile is not found
                throw new UserException("User profile not found.");
            }

            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .message(ApiConstant.IS_SUCCESS)
                    .status(true)
                    .data(userProfile)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (UserException ex) {
            // Handle specific UserException here and return an appropriate error response
            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .message("UserException: " + ex.getMessage())
                    .status(false)
                    .data(null)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            // Handle other exceptions here and return an appropriate error response
            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .message("An unexpected error occurred: " + ex.getMessage())
                    .status(false)
                    .data(null)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
