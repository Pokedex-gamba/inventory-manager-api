package com.github.martmatix.inventorymanagerapi.inventorymanager.controllers;

import com.github.martmatix.inventorymanagerapi.inventorymanager.constants.ErrorCodes;
import com.github.martmatix.inventorymanagerapi.inventorymanager.dtos.PokemonFromGambaDTO;
import com.github.martmatix.inventorymanagerapi.inventorymanager.entities.InventoryEntity;
import com.github.martmatix.inventorymanagerapi.inventorymanager.services.InventoryService;
import com.github.martmatix.inventorymanagerapi.inventorymanager.services.KeyLoaderService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.PublicKey;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ManagerController {

    private InventoryService inventoryService;
    private KeyLoaderService keyLoaderService;

    @PostMapping(path = "/pokemon/inventory/saveGamba")
    public ResponseEntity<?> saveGamba(@RequestHeader("Authorization") String authHeader, @RequestBody PokemonFromGambaDTO pokemon) {
        try {
            String userId = getUserIdFromToken(authHeader);
            if (userId.equals(ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Unable To Process Request: " + ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode() + "\"}");
            }
            if (userId.equals(ErrorCodes.PUBLIC_NOT_FOUND.getCode())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Unable To Process Request: " + ErrorCodes.PUBLIC_NOT_FOUND.getCode() + "\"}");
            }

            inventoryService.saveInventoryEntity(pokemon);

            return ResponseEntity.ok("{\"ok\": \"Pokemon Saved To Database\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Internal Server Error: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping(path = "/pokemon/inventory/getInventory")
    public ResponseEntity<?> getUserGamba(@RequestHeader("Authorization") String authHeader) {
        try {
            String userId = getUserIdFromToken(authHeader);
            if (userId.equals(ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Unable To Process Request: " + ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode() + "\"}");
            }
            if (userId.equals(ErrorCodes.PUBLIC_NOT_FOUND.getCode())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Unable To Process Request: " + ErrorCodes.PUBLIC_NOT_FOUND.getCode() + "\"}");
            }

            List<InventoryEntity> gambaByUserId = inventoryService.getPlayersInventory(userId);
            return ResponseEntity.ok(gambaByUserId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Internal Server Error: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping(path = "/pokemon/inventory/getInventoryById")
    public ResponseEntity<?> getInventoryById(@RequestHeader("Authorization") String authHeader, @RequestParam("inventory") String inventoryId) {
        if (inventoryId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Inventory ID Is Missing In Request\"}");
        }

        String userId = getUserIdFromToken(authHeader);
        if (userId.equals(ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Unable To Process Request: " + ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode() + "\"}");
        }
        if (userId.equals(ErrorCodes.PUBLIC_NOT_FOUND.getCode())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Unable To Process Request: " + ErrorCodes.PUBLIC_NOT_FOUND.getCode() + "\"}");
        }

        Optional<InventoryEntity> inventoryEntity = inventoryService.getEntityById(UUID.fromString(inventoryId));
        if (inventoryEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"warning\": \"No Content: Inventory with this ID does not exist\"}");
        }

        return ResponseEntity.ok(inventoryEntity.get());
    }

    @GetMapping(path = "/pokemon/inventory/getUserTotal")
    public ResponseEntity<?> getUserTotal(@RequestHeader("Authorization") String authHeader) {
        String userId = getUserIdFromToken(authHeader);
        if (userId.equals(ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Unable To Process Request: " + ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode() + "\"}");
        }
        if (userId.equals(ErrorCodes.PUBLIC_NOT_FOUND.getCode())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Unable To Process Request: " + ErrorCodes.PUBLIC_NOT_FOUND.getCode() + "\"}");
        }

        return ResponseEntity.ok(inventoryService.findUserTotal());
    }

    @GetMapping(path = "/pokemon/inventory/changeOwner")
    public ResponseEntity<?> changeOwnership(@RequestParam("inventory") String inventoryId, @RequestParam("user") String newUserId, @RequestHeader("Authorization") String authHeader) {
        try {
            if (inventoryId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Inventory ID Is Missing In Request\"}");
            }

            if (newUserId.trim().isEmpty() || newUserId.trim().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"User ID Is Missing In Request\"}");
            }

            String userId = getUserIdFromToken(authHeader);
            if (userId.equals(ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Unable To Process Request: " + ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode() + "\"}");
            }
            if (userId.equals(ErrorCodes.PUBLIC_NOT_FOUND.getCode())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Unable To Process Request: " + ErrorCodes.PUBLIC_NOT_FOUND.getCode() + "\"}");
            }

            Optional<InventoryEntity> inventoryEntity = inventoryService.getEntityById(UUID.fromString(inventoryId));
            if (inventoryEntity.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"warning\": \"No Content: Inventory with this ID does not exist\"}");
            }
            inventoryEntity.get().setUserId(newUserId);
            inventoryService.updateInventoryEntity(inventoryEntity.get());

            return ResponseEntity.ok("{\"ok\": \"Inventory Updated\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Internal Server Error: " + e.getMessage() + "\"}");
        }
    }

    private String getUserIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer", "").trim();

        PublicKey publicKey;
        try {
            String path = ManagerController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            File publicKeyFile = new File(path, "decoding_key");
            if (!publicKeyFile.exists()) {
                return ErrorCodes.PUBLIC_NOT_FOUND.getCode();
            }
            BufferedReader reader = new BufferedReader(new FileReader(publicKeyFile));
            String publicKeyContent = reader.lines().collect(Collectors.joining("\n"));
            reader.close();
            publicKey = keyLoaderService.getPublicKey(publicKeyContent);
        } catch (Exception e) {
            return ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode();
        }

        Claims claims = Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).getPayload();

        String userId = claims.get("user_id", String.class);
        if (userId == null) {
            return ErrorCodes.TOKEN_EXTRACTION_ERROR.getCode();
        }

        return userId;
    }

    @Autowired
    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Autowired
    public void setKeyLoaderService(KeyLoaderService keyLoaderService) {
        this.keyLoaderService = keyLoaderService;
    }

}
