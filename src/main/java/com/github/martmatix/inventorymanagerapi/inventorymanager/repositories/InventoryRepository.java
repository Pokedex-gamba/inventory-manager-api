package com.github.martmatix.inventorymanagerapi.inventorymanager.repositories;

import com.github.martmatix.inventorymanagerapi.inventorymanager.entities.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {

    List<InventoryEntity> findAllByUserId(String userId);

}
