package com.prod.project.airBnbApp.dto;

import com.prod.project.airBnbApp.entity.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class BookingDto {
    private Long id;
    private Integer roomsCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guests;
}
