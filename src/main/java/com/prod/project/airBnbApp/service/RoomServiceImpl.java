package com.prod.project.airBnbApp.service;

import com.prod.project.airBnbApp.dto.RoomDto;
import com.prod.project.airBnbApp.entity.Hotel;
import com.prod.project.airBnbApp.entity.Room;
import com.prod.project.airBnbApp.exception.ResourceNotFoundException;
import com.prod.project.airBnbApp.repository.HotelRepository;
import com.prod.project.airBnbApp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public RoomDto createNewRoom(Long hotelId,RoomDto roomDto) {
        log.info("creating a new room in hotel with ID:{}",hotelId);
        Hotel hotel= hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with the id: "+hotelId));
        Room room=modelMapper.map(roomDto,Room.class);
        room.setHotel(hotel);
        room=roomRepository.save(room);
       if(hotel.isActive())
       {
           inventoryService.initializeRoomForAYear(room);
       }

        return modelMapper.map(room, RoomDto.class);

    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("fetching all the rooms in the hotel with id {}",hotelId);
        Hotel hotel= hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with the id: "+hotelId));

       return hotel.getRooms()
               .stream()
               .map((element) -> modelMapper.map(element, RoomDto.class))
               .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting the room with ID: {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: "+roomId));
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Deleting the room with ID: {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: "+roomId));
        roomRepository.deleteById(roomId);
        inventoryService.deleteFutureInventories(room);
    }
}
