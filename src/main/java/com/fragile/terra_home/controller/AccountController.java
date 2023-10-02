package com.fragile.terra_home.controller;

import com.fragile.terra_home.dto.request.AccountRequestDto;
import com.fragile.terra_home.dto.response.ApiResponse;
import com.fragile.terra_home.dto.response.BeneficiaryAccountResponseDto;
import com.fragile.terra_home.entities.BeneficiaryAccount;
import com.fragile.terra_home.entities.User;
import com.fragile.terra_home.services.AccountService;
import com.fragile.terra_home.services.CreatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/creators/account")
@RestController
@RequiredArgsConstructor
public class AccountController  {

    private final CreatorService creatorService;

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createAccount(@RequestHeader("Authorization") String jwt, @Valid @RequestBody AccountRequestDto requestDto){
        User user = creatorService.findUserByJwt(jwt);
        BeneficiaryAccountResponseDto bacct = accountService.createAccount(user, requestDto);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message("Account created sucessfully")
                .status(true)
                .data(bacct)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
