package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.AccountRequestDto;
import com.fragile.terra_home.dto.response.BeneficiaryAccountResponseDto;
import com.fragile.terra_home.entities.BeneficiaryAccount;
import com.fragile.terra_home.entities.User;
import com.fragile.terra_home.exceptions.ResourceNotFoundException;
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

    @Override
    public BeneficiaryAccount getAccount(User creator) {
        try {
            User user = creatorService.findUserByEmail(creator.getEmail());
            if (isPaymentAccountCreated(user.getCreatorAccount())) {
                return user.getCreatorAccount();
            }
            throw new ResourceNotFoundException("No payment Account created for this user, create a new payment account");
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error: " + ex.getMessage(), ex);
        }

    }

    @Override
    public BeneficiaryAccount updateAccount(User creator, AccountRequestDto requestDto) {
        User user = creatorService.findUserByEmail(creator.getEmail());
        BeneficiaryAccount acct = user.getCreatorAccount();
        if (isPaymentAccountCreated(acct)) {
            acct.setBankName(requestDto.getBankName());
            acct.setAccountHolderName(requestDto.getAccountHolderName());
            acct.setBankName(requestDto.getBankName());
            return accountRepository.save(acct);
        }
        throw new ResourceNotFoundException("No payment Account created for this user, create a new payment account");
    }

    private Boolean isPaymentAccountCreated(BeneficiaryAccount acct) {
        if (Objects.isNull(acct)) {
            throw new ResourceNotFoundException("No payment Account created for this user, create a new payment account");
        }
        return true;

    }

}
