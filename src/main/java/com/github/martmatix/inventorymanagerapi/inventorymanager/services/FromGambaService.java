package com.github.martmatix.inventorymanagerapi.inventorymanager.services;

import com.github.martmatix.inventorymanagerapi.inventorymanager.dtos.PokemonFromGambaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class FromGambaService {

    private WebClient.Builder builder;

    @Value("${pokemon.gamba.url}")
    private String gambaHost;

    public Flux<PokemonFromGambaDTO> getPokemonFromGamba(String authHeader) {
        WebClient webClient = builder.baseUrl(gambaHost).build();

        return webClient.get()
                .uri("/pokemon/gamba/getRandomPokemon")
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToFlux(PokemonFromGambaDTO.class);
    }

    @Autowired
    public void setBuilder(WebClient.Builder builder) {
        this.builder = builder;
    }
}
