package com.github.martmatix.inventorymanagerapi.inventorymanager.services;

import com.github.martmatix.inventorymanagerapi.inventorymanager.dtos.PokemonFromGambaDTO;
import com.github.martmatix.inventorymanagerapi.inventorymanager.entities.InventoryEntity;
import com.github.martmatix.inventorymanagerapi.inventorymanager.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {

    private InventoryRepository inventoryRepository;

    public void saveInventoryEntity(PokemonFromGambaDTO pokemon) {
        InventoryEntity inventoryEntity = new InventoryEntity();

        inventoryEntity.setUserId(pokemon.getUserId());
        inventoryEntity.setPokemonName(pokemon.getPokemonName());
        inventoryEntity.setBaseHP(pokemon.getBaseHP());
        inventoryEntity.setBaseAttack(pokemon.getBaseAttack());
        inventoryEntity.setBaseDefense(pokemon.getBaseDefense());
        inventoryEntity.setBaseSpeed(pokemon.getBaseSpeed());
        inventoryEntity.setLegendary(pokemon.isLegendary());
        inventoryEntity.setType(pokemon.getType());
        inventoryEntity.setTotalRarity(pokemon.getTotalRarity());
        inventoryEntity.setFrontDefault(pokemon.getFrontDefault());
        inventoryEntity.setFrontShiny(pokemon.getFrontShiny());

        inventoryRepository.save(inventoryEntity);
    }

    public void updateInventoryEntity(InventoryEntity inventoryEntity) {
        inventoryRepository.save(inventoryEntity);
    }

    public List<InventoryEntity> getPlayersInventory(String userId) {
        return inventoryRepository.findAllByUserId(userId);
    }

    public Optional<InventoryEntity> getEntityById(UUID inventoryId) {
        return inventoryRepository.findById(inventoryId);
    }

    @Autowired
    public void setInventoryRepository(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
}
