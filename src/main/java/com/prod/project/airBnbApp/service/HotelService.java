package com.prod.project.airBnbApp.service;

import com.prod.project.airBnbApp.dto.HotelDto;
import com.prod.project.airBnbApp.dto.HotelInfoDto;
import org.jspecify.annotations.Nullable;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id,HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel(Long hotelId);


    HotelInfoDto getHotelInfoById(Long hotelId);
}
