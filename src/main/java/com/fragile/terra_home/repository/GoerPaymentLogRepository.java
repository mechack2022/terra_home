package com.fragile.terra_home.repository;

import com.fragile.terra_home.entities.Goer;
import com.fragile.terra_home.entities.GoerPaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoerPaymentLogRepository extends JpaRepository<GoerPaymentLog, Long> {

  Optional<GoerPaymentLog> findByGoer(Goer goer);

}
