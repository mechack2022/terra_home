package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.AccountRequestDto;
import com.fragile.terra_home.dto.response.BeneficiaryAccountResponseDto;
import com.fragile.terra_home.entities.BeneficiaryAccount;
import com.fragile.terra_home.entities.User;
import com.fragile.terra_home.exceptions.UserException;
import com.fragile.terra_home.repository.AccountRepository;
import com.fragile.terra_home.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final CreatorService creatorService;
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public BeneficiaryAccountResponseDto createAccount(User creator, AccountRequestDto req) {
        try {
            User user = userRepository.findByEmail(creator.getEmail());
            if (!Objects.isNull(user)) {
                // create a new account for the user
                BeneficiaryAccount account = new BeneficiaryAccount();
                account.setCreator(user);
                account.setAccountNumber(req.getAccountNumber());
                account.setAccountHolderName(req.getAccountHolderName());
                account.setBankName(req.getBankName());

                BeneficiaryAccount savedAccount = accountRepository.save(account);
                // persist creator with account
                user.setCreatorAccount(savedAccount);
                userRepository.save(user);
                return modelMapper.map(savedAccount, BeneficiaryAccountResponseDto.class);
            }
            throw new UserException("User not found");
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error: " + ex.getMessage(), ex);
        }
    }
}
