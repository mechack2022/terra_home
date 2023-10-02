package com.fragile.terra_home.repository;

import com.fragile.terra_home.entities.BeneficiaryAccount;
import com.fragile.terra_home.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<BeneficiaryAccount, Long> {

    BeneficiaryAccount findByCreator(User creator);
}
