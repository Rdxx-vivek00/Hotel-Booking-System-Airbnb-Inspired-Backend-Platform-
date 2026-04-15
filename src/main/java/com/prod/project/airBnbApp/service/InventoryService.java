package com.prod.project.airBnbApp.service;

import com.prod.project.airBnbApp.dto.HotelPriceDto;
import com.prod.project.airBnbApp.dto.HotelSearchRequest;
import com.prod.project.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
