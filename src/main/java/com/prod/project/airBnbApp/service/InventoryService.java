package com.prod.project.airBnbApp.service;

import com.prod.project.airBnbApp.entity.Room;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteFutureInventories(Room room);

}
