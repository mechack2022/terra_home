package com.fragile.terra_home.repository;

import com.fragile.terra_home.entities.Goer;
import com.fragile.terra_home.entities.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoerPaymentLogRepository extends JpaRepository<PaymentLog, Long> {

  Optional<PaymentLog> findByGoer(Goer goer);

}
