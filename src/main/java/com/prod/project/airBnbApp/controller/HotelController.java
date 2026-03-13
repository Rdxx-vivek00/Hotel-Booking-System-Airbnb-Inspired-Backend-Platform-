package com.prod.project.airBnbApp.controller;

import com.prod.project.airBnbApp.dto.HotelDto;
import com.prod.project.airBnbApp.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotelDto)
    {
     log.info("attempting to create a new hotel with name {}",hotelDto.getName());
     HotelDto hotel=hotelService.createNewHotel(hotelDto);

     return new ResponseEntity<>(hotel, HttpStatus.CREATED);

    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId)
    {

        HotelDto hotel=hotelService.getHotelById(hotelId);
        return new ResponseEntity<>(hotel,HttpStatus.OK);
    }
}
