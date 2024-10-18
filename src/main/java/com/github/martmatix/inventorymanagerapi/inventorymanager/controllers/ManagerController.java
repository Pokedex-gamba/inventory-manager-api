package com.github.martmatix.inventorymanagerapi.inventorymanager.controllers;

import com.github.martmatix.inventorymanagerapi.inventorymanager.dtos.PokemonFromGambaDTO;
import com.github.martmatix.inventorymanagerapi.inventorymanager.services.FromGambaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ManagerController {

    private FromGambaService fromGambaService;

    @GetMapping(path = "/pokemon/inventory/getFromGamba")
    public ResponseEntity<?> getPokemonFromGamba(@RequestHeader("Authorization") String authHeader) {
        try {
            Flux<PokemonFromGambaDTO> pokemonFlux = fromGambaService.getPokemonFromGamba(authHeader);
            Mono<List<PokemonFromGambaDTO>> pokemonMono = pokemonFlux.collectList();

            List<PokemonFromGambaDTO> pokemons = pokemonMono.block();

            return ResponseEntity.ok(pokemons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Internal Server Error: " + e.getMessage() + "\"}");
        }
    }

    @Autowired
    public void setFromGambaService(FromGambaService fromGambaService) {
        this.fromGambaService = fromGambaService;
    }
}
