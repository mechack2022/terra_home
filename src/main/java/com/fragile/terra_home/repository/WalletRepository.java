package com.fragile.terra_home.repository;

import com.fragile.terra_home.entities.User;
import com.fragile.terra_home.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser(User user);

}
