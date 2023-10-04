package com.fragile.terra_home.repository;

import com.fragile.terra_home.entities.Goer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoerRepository extends JpaRepository<Goer, Long> {
    Optional<Goer> findByEmail(String email);


}
