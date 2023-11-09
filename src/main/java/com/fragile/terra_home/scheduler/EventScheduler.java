package com.fragile.terra_home.scheduler;

import com.fragile.terra_home.constants.EventStatus;
import com.fragile.terra_home.entities.Event;
import com.fragile.terra_home.repository.EventRepository;
import com.fragile.terra_home.services.EventServices;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventScheduler {

    private  final EventRepository eventRepository;
    @Scheduled(cron = "0 */5 * * * *")
    public void deactivateExpiredEvents() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        eventRepository.findAll().stream()
                .filter(event -> !isEventActive(event, currentDateTime))
                .forEach(this::deactivateEvent);
    }

    private void deactivateEvent(Event event) {
        // Deactivate the event (update its status, perform any necessary actions)
        event.setEventStatus(EventStatus.CLOSED);
        eventRepository.save(event);
    }

    private boolean isEventActive(Event event, LocalDateTime currentDateTime) {
        // Check if current date and time are within the event's start and end dates
        boolean isDateInRange = currentDateTime.isAfter(event.getStartDate()) && currentDateTime.isBefore(event.getEndDate());

        // Check if the current time is within the event's start and end times
        LocalTime currentTime = currentDateTime.toLocalTime();
        boolean isTimeInRange = currentTime.isAfter(event.getStartTime()) && currentTime.isBefore(event.getEndTime());

        // Return true if both date and time are within the ranges
        return isDateInRange && isTimeInRange;
    }

}
