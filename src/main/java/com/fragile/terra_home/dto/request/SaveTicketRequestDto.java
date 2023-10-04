package com.fragile.terra_home.dto.request;

import com.fragile.terra_home.constants.UserRole;
import com.fragile.terra_home.entities.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveTicketRequestDto {
    private String firstName;
    private String email;
    private Long eventId;
    private List<Ticket> ticketList;
    private LocalDateTime createdAt;


}
