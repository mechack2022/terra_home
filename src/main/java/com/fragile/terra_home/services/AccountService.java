package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.AccountRequestDto;
import com.fragile.terra_home.dto.response.BeneficiaryAccountResponseDto;
import com.fragile.terra_home.entities.User;

public interface AccountService {
    BeneficiaryAccountResponseDto createAccount(User creator, AccountRequestDto req);
}
