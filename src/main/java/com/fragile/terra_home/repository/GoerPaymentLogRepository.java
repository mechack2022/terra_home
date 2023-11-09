package com.fragile.terra_home.repository;

import com.fragile.terra_home.entities.Goer;
import com.fragile.terra_home.entities.GoerPayLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoerPaymentLogRepository extends JpaRepository<GoerPayLog, Long> {

  Optional<GoerPayLog> findByGoer(Goer goer);

    Optional<GoerPayLog> findByGoerId(Long goerId);
}
