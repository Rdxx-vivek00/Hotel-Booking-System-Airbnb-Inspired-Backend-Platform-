package com.prod.project.airBnbApp.repository;

import com.prod.project.airBnbApp.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
}
