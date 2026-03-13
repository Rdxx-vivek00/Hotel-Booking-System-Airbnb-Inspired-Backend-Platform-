package com.prod.project.airBnbApp.service;

import com.prod.project.airBnbApp.dto.HotelDto;
import com.prod.project.airBnbApp.entity.Hotel;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);


}
