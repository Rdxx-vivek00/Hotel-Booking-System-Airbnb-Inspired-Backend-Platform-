package com.prod.project.airBnbApp.service;

import com.prod.project.airBnbApp.dto.HotelDto;
import com.prod.project.airBnbApp.entity.Hotel;
import com.prod.project.airBnbApp.entity.Room;
import com.prod.project.airBnbApp.exception.ResourceNotFoundException;
import com.prod.project.airBnbApp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService{

private final HotelRepository hotelRepository;
private final InventoryService inventoryService;
private final ModelMapper modelMapper;

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
    public void deleteHotelById(Long id) {
        Hotel hotel= hotelRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with the id: "+id));


        hotelRepository.deleteById(id);
        for(Room room:hotel.getRooms())
        {
            inventoryService.deleteFutureInventories(room);
        }

    }

    @Override
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
}
