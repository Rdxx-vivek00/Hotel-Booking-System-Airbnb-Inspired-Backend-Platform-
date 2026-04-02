package com.prod.project.airBnbApp.service;

import com.prod.project.airBnbApp.dto.BookingDto;
import com.prod.project.airBnbApp.dto.BookingRequest;
import com.prod.project.airBnbApp.dto.GuestDto;

import java.util.List;

public interface BookingService {
    BookingDto initializeBooking(BookingRequest bookingRequest);
    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
