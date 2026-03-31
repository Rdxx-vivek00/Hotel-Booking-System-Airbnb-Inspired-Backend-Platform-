package com.prod.project.airBnbApp.controller;

import com.prod.project.airBnbApp.dto.HotelDto;
import com.prod.project.airBnbApp.dto.HotelInfoDto;
import com.prod.project.airBnbApp.dto.HotelSearchRequest;
import com.prod.project.airBnbApp.service.HotelService;
import com.prod.project.airBnbApp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

private final InventoryService inventoryService;
private final HotelService hotelService;

@GetMapping("/search")
public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest)
{
    Page<HotelDto> page=inventoryService.searchHotels(hotelSearchRequest);
    return ResponseEntity.ok(page);
}

@GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId)
{
    return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
}

}
