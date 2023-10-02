package com.fragile.terra_home.repository;

import com.fragile.terra_home.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fragile.terra_home.entities.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventCreator(User eventCreator);
}
