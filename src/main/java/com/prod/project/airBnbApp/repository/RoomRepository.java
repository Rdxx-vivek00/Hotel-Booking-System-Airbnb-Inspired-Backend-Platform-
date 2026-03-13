package com.prod.project.airBnbApp.repository;

import com.prod.project.airBnbApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
