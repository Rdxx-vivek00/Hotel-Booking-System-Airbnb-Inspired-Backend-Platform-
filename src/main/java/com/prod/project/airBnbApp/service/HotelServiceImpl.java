package com.prod.project.airBnbApp.service;

import com.prod.project.airBnbApp.dto.HotelDto;
import com.prod.project.airBnbApp.dto.HotelInfoDto;
import com.prod.project.airBnbApp.dto.RoomDto;
import com.prod.project.airBnbApp.entity.Hotel;
import com.prod.project.airBnbApp.entity.Room;
import com.prod.project.airBnbApp.exception.ResourceNotFoundException;
import com.prod.project.airBnbApp.repository.HotelRepository;
import com.prod.project.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService{

private final HotelRepository hotelRepository;
private final InventoryService inventoryService;
private final ModelMapper modelMapper;
private final RoomRepository roomRepository;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("creating a hotel with name :{}",hotelDto.getName());

        Hotel hotel=modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);
        log.info("created a hotel with Id :{}",hotel.getId());
        return modelMapper.map(hotelRepository.save(hotel),HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("fetching the hotel with id: {}",id);
       Hotel hotel= hotelRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with the id: "+id));

       return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id,HotelDto hotelDto) {
        log.info("updating the hotel with id: {}",id);
        Hotel hotel= hotelRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with the id: "+id));
       modelMapper.map(hotelDto,hotel);
       hotel.setId(id);
       hotel=hotelRepository.save(hotel);
       return modelMapper.map(hotel,HotelDto.class);

    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel= hotelRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with the id: "+id));


        for(Room room:hotel.getRooms())
        {
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());

        }
        hotelRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("activating the hotel with id: {}",hotelId);
        Hotel hotel= hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with the id: "+hotelId));

        hotel.setActive(true);

        //assuming only doing it once
        for(Room room:hotel.getRooms())
        {
            inventoryService.initializeRoomForAYear(room);
        }




    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
       log.info("getting the hotel  by id {} :",hotelId);
       Hotel hotel=hotelRepository.findById(hotelId)
               .orElseThrow(()->new ResourceNotFoundException("hotel with the id not found"+hotelId));

       List<RoomDto> rooms=hotel.getRooms()
               .stream()
               .map((element)->modelMapper.map(element, RoomDto.class))
               .toList();

       return new HotelInfoDto(modelMapper.map(hotel,HotelDto.class),rooms);

    }
}
