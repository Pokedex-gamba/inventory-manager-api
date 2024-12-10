package com.github.martmatix.inventorymanagerapi.inventorymanager.repositories;

import com.github.martmatix.inventorymanagerapi.inventorymanager.entities.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {

    List<InventoryEntity> findAllByUserId(String userId);

    Optional<InventoryEntity> findById(UUID id);

    @Query(value = "select user_id, sum(total_rarity) from inventory group by user_id", nativeQuery = true)
    List<Object[]> findUserTotal();

    Optional<InventoryEntity> findByPokemonName(String pokemonName);

}
