package com.fragile.terra_home.repository;

import com.fragile.terra_home.entities.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<EventCategory, Long> {

   Optional<EventCategory> findByCategoryName(String name);
}
