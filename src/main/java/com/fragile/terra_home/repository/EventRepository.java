package com.fragile.terra_home.repository;

import com.fragile.terra_home.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fragile.terra_home.entities.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventCreator(User eventCreator);

    @Query("SELECT e FROM Event e WHERE " +
            "(:categoryName IS NULL OR e.category.categoryName = :categoryName) OR " +
            "(:location IS NULL OR e.location = :location) OR " +
            "(:date IS NULL OR e.startDate <= :date)")
    List<Event> filterEventsByCategoryLocationDate(
            @Param("categoryName") String categoryName,
            @Param("location") String location,
            @Param("date") LocalDateTime date
    );


//    List<Event> findByCategoryIgnoreCaseOrLocationIgnoreCaseOrStartDate(EventCategory category, String location, LocalDateTime startDate);

}
