package com.prod.project.airBnbApp.repository;

import com.prod.project.airBnbApp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface GuestRepository extends JpaRepository<Guest, Long> {

}