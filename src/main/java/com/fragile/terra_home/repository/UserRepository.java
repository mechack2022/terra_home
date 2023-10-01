package com.fragile.terra_home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fragile.terra_home.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
   User findByEmail(String email);
}