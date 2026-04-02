package com.prod.project.airBnbApp.service;

import com.prod.project.airBnbApp.dto.BookingDto;
import com.prod.project.airBnbApp.dto.BookingRequest;
import com.prod.project.airBnbApp.dto.GuestDto;
import com.prod.project.airBnbApp.entity.*;
import com.prod.project.airBnbApp.entity.enums.BookingStatus;
import com.prod.project.airBnbApp.exception.ResourceNotFoundException;
import com.prod.project.airBnbApp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    @Override
    public BookingDto initializeBooking(BookingRequest bookingRequest) {
       log.info("Initializing booking for hotel : {}, room : {}, date {}-{}",bookingRequest.getHotelId(),bookingRequest.getRoomId(),bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate());
        Hotel hotel=hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id"+bookingRequest.getHotelId()));
        Room room=roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(()->new ResourceNotFoundException("room not found with the id"+bookingRequest.getRoomId()));
        List<Inventory> inventoryList=inventoryRepository.findAndLockAvailableInventory(room.getId(),bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate(),bookingRequest.getRoomsCount());
        Long daysCount= ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate())+1;

        if(inventoryList.size()!=daysCount)
        {
            throw new IllegalStateException("room is not available anymore");
        }

        for(Inventory inventory:inventoryList)
        {
            inventory.setReservedCount(inventory.getReservedCount()+bookingRequest.getRoomsCount());
        }
        inventoryRepository.saveAll(inventoryList);

        Booking booking=Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(getCurrentUser())
                .roomsCount(bookingRequest.getRoomsCount())
                .amount(BigDecimal.TEN)
                .build();

        booking=bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    @Transactional
    @Override
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {
        log.info("adding guest for booking with id : "+bookingId);
        Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new ResourceNotFoundException("booking not found with id "+bookingId));
        if(hasBookingExpired(booking))
        {
            throw new IllegalStateException("Booking has already expired");
        }

        if(booking.getBookingStatus()!=BookingStatus.RESERVED)
        {
            throw new IllegalStateException("Booking is not under reserved state, cannot add guests");
        }

        for(GuestDto guestDto:guestDtoList)
        {
            Guest guest=modelMapper.map(guestDto,Guest.class);
            guest.setUser(getCurrentUser());
             guest=guestRepository.save(guest);
            booking.getGuests().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking=bookingRepository.save(booking);
        return modelMapper.map(booking,BookingDto.class);
    }

    public User getCurrentUser() {
        User user = new User();
        user.setId(1L); // TODO: REMOVE DUMMY USER
        return user;

    }

    public boolean hasBookingExpired(Booking booking) {
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }
}
