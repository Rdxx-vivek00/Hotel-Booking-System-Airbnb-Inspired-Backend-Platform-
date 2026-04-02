package com.prod.project.airBnbApp.controller;

import com.prod.project.airBnbApp.dto.BookingDto;
import com.prod.project.airBnbApp.dto.BookingRequest;
import com.prod.project.airBnbApp.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initializeBooking(@RequestBody BookingRequest bookingRequest)
    {
        return ResponseEntity.ok(bookingService.initializeBooking(bookingRequest));
    }



}
